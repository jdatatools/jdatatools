package com.ainouss.datatools.jdatatools.reader;

import com.ainouss.datatools.jdatatools.data.Chunk;
import com.ainouss.datatools.jdatatools.query.CriteriaQuery;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Template processor
 *
 * @param <T> input POJO
 * @param <R> return POJO
 */
@Data
@Builder
@Slf4j
public class JdbcReadTemplate<T, R> {
    /**
     * Select SQL query
     */
    private String select;
    /**
     * Input Pojo class
     */
    private Class<T> clazz;
    /**
     * Source reader
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Mapper from input pojo to return pojo
     */
    private Function<T, ? extends R> mapper;
    /**
     * Async consumer
     */
    private Consumer<Chunk<? extends R>> consumer;

    /**
     * Fetch size
     */
    private int fetchSize;
    /**
     * Count query result
     */
    private Integer count;

    /**
     *
     */
    private CriteriaQuery<T> criteria;

    /**
     * is true, then the processing of the template will be canceled mid-way
     */
    @Builder.Default
    private AtomicBoolean isCanceled = new AtomicBoolean(false);

    @Builder.Default
    private boolean cancelOnFailure = false;

    public void setConsumer(Consumer<Chunk<? extends R>> consumer) {
        this.consumer = (v) -> {
            if (!isCanceled.get()) {
                try {
                    consumer.accept(v);
                } catch (Exception e) {
                    isCanceled.set(cancelOnFailure);
                    log.error("failed to consume data", e);
                    throw e;
                }
            }
        };
    }

    private String correlationId;

}
