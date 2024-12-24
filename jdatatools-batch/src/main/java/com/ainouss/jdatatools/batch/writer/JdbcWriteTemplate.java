package com.ainouss.jdatatools.batch.writer;

import com.ainouss.jdatatools.query.core.CriteriaQuery;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Template processor
 */
@Data
@Builder
public class JdbcWriteTemplate {

    /**
     * Input Pojo class
     */
    private Class<?> clazz;
    /**
     * Insert SQL query
     */
    private String insert;
    /**
     * Target table
     */
    private String targetTable;
    /**
     * Target writer
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Criteria
     */

    private CriteriaQuery<?> criteria;

    /**
     * Transaction manager
     */

    private PlatformTransactionManager txManager;
    /**
     * Correlation id for logging
     */
    private String correlationId;
}

