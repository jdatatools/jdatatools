package com.ainouss.datatools.jdatatools.reader;

import com.ainouss.datatools.jdatatools.data.Chunk;
import com.ainouss.datatools.jdatatools.query.core.CriteriaBuilder;
import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;
import com.ainouss.datatools.jdatatools.util.QueryBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ainouss.datatools.jdatatools.reader.RowExtractor.extract;
import static com.ainouss.datatools.jdatatools.util.DataUtils.isBlank;

/**
 * An opinionated implementation of reading data from a database through a jdbc template.
 * Replaces an SQL string-based approach to read data
 * Instead, queries are compiled from entity definitions re-using the same specifications as JPA
 * Some functionalities are inspired from Hibernate criteria builder
 */

@Component
@Slf4j
@AllArgsConstructor
public class JdbcChunkReader {
    /**
     * max fetch size
     */
    private static final int MAX_CHUNK_SIZE = 1_000_000;
    /**
     * Min fetch size
     */
    private static final int MIN_CHUNK_SIZE = 1000;

    /**
     * Reads data from a table using criteria through chunking:
     * - counts the total result by issuing count query
     * - fetches data in parallel as a completable future
     * - returns a future that completed when all sub-futures are completed
     *
     * @param template input template
     * @param <T>      input type class
     * @param <R>      return type class
     * @return a completable future
     */
    public <T, R> CompletableFuture<List<Void>> process(JdbcReadTemplate<T, R> template) {
        if (template == null || template.getJdbcTemplate() == null) {
            throw new RuntimeException("jdbc template (reader) should not be null");
        }
        if (template.getCriteria() == null) {
            CriteriaBuilder cb = EntityRegistry.criteriaBuilder();
            CriteriaQuery<T> cr = cb.createQuery(template.getClazz());
            template.setCriteria(cr);
        }
        if (isBlank(template.getSelect())) {
            template.setSelect(template.getCriteria().buildSelectQuery());
        }

        if (template.getCount() == null) {
            String countQuery = template.getCriteria().buildCountQuery();
            Integer count = query(countQuery, template.getJdbcTemplate(), Integer.class, template.getCorrelationId());
            template.setCount(count);
        }
        if (template.getFetchSize() == 0) {
            int chunkSize = 1;
            if (template.getCount() != null) {
                chunkSize = (template.getCount() / Runtime.getRuntime().availableProcessors()) + 1;
            }
            if (chunkSize > MAX_CHUNK_SIZE) {
                chunkSize = MAX_CHUNK_SIZE;
            }
            if (chunkSize < MIN_CHUNK_SIZE) {
                chunkSize = MIN_CHUNK_SIZE;
            }
            template.setFetchSize(chunkSize);
        }
        if (template.getConsumer() == null) {
            template.setConsumer(chunk -> {
            });
        }
        int start = 0;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        final List<Field> fields = template.getCriteria().getFields();
        while (start < template.getCount()) {
            final int row = start;
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> getChunk(template, row, fields)).thenAccept(template.getConsumer());
            futures.add(future);
            start = start + template.getFetchSize();
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * Select query executor
     *
     * @param <R>           type
     * @param query         sql query
     * @param jdbc          reader jdbc template
     * @param clazz         return type
     * @param correlationId correlation id
     * @return result
     */
    public <R> R query(String query, JdbcTemplate jdbc, Class<R> clazz, String correlationId) {
        R result = jdbc.queryForObject(query, clazz);
        log.info("ID-{} completed {}, result {}", correlationId, query, result);
        return result;
    }


    /**
     * @param template read template
     * @param <T>      input type
     * @param <R>      return type
     * @return chunk of data
     */
    public <T, R> Chunk<R> read(JdbcReadTemplate<T, R> template) {
        if (template == null || template.getJdbcTemplate() == null) {
            throw new RuntimeException("jdbc template should not be null");
        }
        if (template.getCriteria() == null) {
            throw new RuntimeException("criteria should not be null");
        }
        if (isBlank(template.getSelect())) {
            template.setSelect(template.getCriteria().buildSelectQuery());
        }
        if (template.getClazz() == null) {
            template.setClazz(template.getCriteria().getResultType());
        }
        return getChunk(template, 0, null);
    }

    /**
     * Fetches a list of data and transforms it to a given class
     *
     * @param <T>    type
     * @param fields Fields
     * @param start  start row
     * @return the list of data
     */
    private <T, R> Chunk<R> getChunk(JdbcReadTemplate<T, R> template, final int start, List<Field> fields) {
        var jdbc = template.getJdbcTemplate();
        var clazz = template.getClazz();
        var mapper = template.getMapper();
        String sql = template.getSelect();
        if (!(start == 0 && template.getFetchSize() == 0)) {
            sql = template.getSelect().concat(QueryBuilder.buildOffsetQuery(start, template.getFetchSize()));
        }
        String tableName = QueryBuilder.getTableNameFromSelectQuery(sql);
        int end = start + template.getFetchSize();
        if (template.getCount() != null && template.getCount() < end) {
            end = template.getCount();
        }
        Chunk<R> chunk = new Chunk<>(new ArrayList<>(), start, end);
        if (template.getCount() == null) {
            log.info("ID-{} started reading records [{} - ?] from {}", template.getCorrelationId(), start, tableName);
        } else {
            log.info("ID-{} started reading records [{} - {}] from {}", template.getCorrelationId(), start, end, tableName);
        }
        jdbc.setFetchSize(template.getFetchSize() == 0 ? MIN_CHUNK_SIZE : template.getFetchSize());
        if (fields == null) {
            fields = template.getCriteria().getFields();
        }
        List<Field> finalFields = fields;
        jdbc.query(sql, resultSet -> {
            do {
                chunk.getData().add(extract(resultSet, clazz, finalFields, mapper, template.getCorrelationId()));
            } while (resultSet.next());
        });
        int total = chunk.getData().size();
        log.info("ID-{} completed reading records [{} - {}] from {}", template.getCorrelationId(), start, start + total, tableName);
        return chunk;
    }

}
