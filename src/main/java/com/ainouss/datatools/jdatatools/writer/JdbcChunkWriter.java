package com.ainouss.datatools.jdatatools.writer;

import com.ainouss.datatools.jdatatools.data.Chunk;
import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;
import com.ainouss.datatools.jdatatools.query.core.CriteriaBuilder;
import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.util.QueryBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * JDBC writer, uses a jdbc template to write through a DataSource
 * Generates SQL insert from a {@link CriteriaQuery} if {@link JdbcWriteTemplate} has no insert sql (named) query.
 * Uses a named query to batch insert with a new transaction.
 * Uses a device and conquer to handle batch failures :
 * the transaction is roll-backed, then the data is split into smaller parts (10 parts)
 * The same mechanism is applied until the process is completed
 * When the size of the chunk is one element and the transaction has been roll-backed, the record is ignored.
 */

@Component
@Slf4j
@AllArgsConstructor
public class JdbcChunkWriter {

    /**
     * JDBC writer, uses a jdbc template to write through a DataSource
     *
     * @param template template
     * @param chunk    chunk of data
     * @param <T>      type
     */
    public <T> void process(JdbcWriteTemplate template, Chunk<? extends T> chunk) {
        normalize(template);
        if (chunk.isEmpty()) {
            log.info("ID-{} empty chunk of {}, nothing to insert", template.getCorrelationId(), template.getTargetTable());
            return;
        }
        insertChunk(template.getInsert(), chunk, template.getJdbcTemplate(), template.getTxManager(), template.getCorrelationId());
    }

    /**
     * Initialize template if not set
     *
     * @param template template
     */
    private void normalize(JdbcWriteTemplate template) {
        if (template == null || template.getJdbcTemplate() == null) {
            throw new RuntimeException("Template writer should not be null");
        }
        EntityRegistry.registerClass(template.getClazz());
        if (template.getTxManager() == null) {
            var tx = new DataSourceTransactionManager(Objects.requireNonNull(template.getJdbcTemplate().getDataSource()));
            template.setTxManager(tx);
        }
        if (template.getCriteria() == null) {
            CriteriaBuilder cb = EntityRegistry.criteriaBuilder();
            CriteriaQuery<?> cr = cb.createQuery(template.getClazz());
            template.setCriteria(cr);
        }
        if (StringUtils.isBlank(template.getInsert())) {
            template.setInsert(template.getCriteria().buildInsertQuery());
        }
        if (StringUtils.isBlank(template.getTargetTable())) {
            String target = QueryBuilder.getTableNameFromInsertQuery(template.getInsert());
            template.setTargetTable(target);
        }
    }


    /**
     * Start a new transaction to insert a chunk of data
     * in the case of a failure, the transaction is roll-backed, then the data is split into smaller parts (10 parts)
     * The same mechanism is applied until the process is completed
     * When the size of the chunk is one element and the transaction has been roll-backed, the record is ignored.
     * <p>
     * see fetch-size
     *
     * @param chunk         chunk of data
     * @param txManager     transaction manager
     * @param correlationId correlation ID
     */

    public void insertChunk(String sql, Chunk<?> chunk, JdbcTemplate jdbc, PlatformTransactionManager txManager, String correlationId) {
        if (chunk.isEmpty()) {
            return;
        }
        var data = chunk.getData();
        TransactionStatus tx = run(sql, chunk, jdbc, txManager, correlationId);
        if (tx != null && tx.isRollbackOnly()) {
            if (data.isEmpty()) {
                log.info("ID-{} empty for query of {}, nothing to insert", correlationId, sql);
                return;
            }
            if (data.size() == 1) {
                log.trace("ID-{} record {} was rolled back", correlationId, data);
                return;
            }
            int size = (int) Math.ceil((double) data.size() / 10);
            ArrayList<?> all = new ArrayList<>(chunk.getData());
            ListUtils.partition(all, size).forEach(sublist -> {
                Chunk<?> subChunk = new Chunk<>(sublist, chunk.getStart(), chunk.getEnd());
                insertChunk(sql, subChunk, jdbc, txManager, correlationId);
            });
        }
    }

    /**
     * Start a new transaction to insert a chunk of data
     *
     * @param sql           sql
     * @param chunk         chunk
     * @param jdbc          jdbc template
     * @param txManager     transaction manager
     * @param correlationId correlation ID
     * @return transaction status
     */
    private TransactionStatus run(String sql, Chunk<?> chunk, JdbcTemplate jdbc, PlatformTransactionManager txManager, String correlationId) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(status -> {
            try {
                insert(sql, chunk, jdbc, correlationId);
                status.flush();
            } catch (DataAccessException e) {
                String table = QueryBuilder.getTableNameFromInsertQuery(sql);
                if (chunk.getData().size() == 1) {
                    log.error("ID-{} could not insert into table {}, record {} Error : {}", correlationId, table, chunk.getData(), e.getMessage());
                    if (e.getCause() instanceof SQLException) {
                        log.warn("ID-{} SQL exception : {}", correlationId, e.getCause().getMessage());
                    }
                } else {
                    log.warn("ID-{} could not insert into table {}, batch size : {}", correlationId, table, chunk.getData().size());
                }
                status.setRollbackOnly();
                chunk.setError(e.getMessage());
            }
            return status;
        });
    }

    /**
     * Insert into with SQL named query & jdbcTemplate
     *
     * @param chunk         chunk
     * @param correlationId correlationId
     */
    private void insert(String sql, Chunk<?> chunk, JdbcTemplate writer, String correlationId) {
        String table = QueryBuilder.getTableNameFromInsertQuery(sql);
        long startTime = System.nanoTime();
        Collection<?> data = chunk.getData();
        writer.setFetchSize(data.size());
        if (chunk.getSize() == 1) {
            log.info("ID-{} started inserting 1 record into {}", correlationId, table);
        } else {
            log.info("ID-{} started inserting {} records [{} - {}] into {}", correlationId,data.size(), chunk.getStart(), chunk.getEnd(), table);
        }
        SqlParameterSource[] parameterSource = SqlParameterSourceUtils.createBatch(data.toArray());
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(writer);
        namedParameterJdbcTemplate.batchUpdate(sql, parameterSource);
        long duration = (System.nanoTime() - startTime) / 1000000;
        if (chunk.getSize() == 1) {
            log.info("ID-{} completed inserting 1 record into {}, duration (ss:SSS) {}",correlationId, table, DurationFormatUtils.formatDuration(duration, "ss:SSS", true));
        } else {
            log.info("ID-{} completed inserting {} records [{} - {}] into {}, duration (ss:SSS) {}", correlationId,data.size(), chunk.getStart(), chunk.getEnd(), table, DurationFormatUtils.formatDuration(duration, "ss:SSS", true));
        }
    }

    /**
     * execute SQL query
     *
     * @param query query
     * @param jdbc  reader
     */

    public void execute(String query, JdbcTemplate jdbc) {
        long startTime = System.nanoTime();
        jdbc.execute(query);
        long duration = (System.nanoTime() - startTime) / 1000000;
        log.info("completed {}, ", query, DurationFormatUtils.formatDuration(duration, "ss:SSS", true));
    }
}
