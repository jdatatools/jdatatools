package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class Subquery extends Alias implements Source {

    private final CriteriaQuery<?> cr;
    private final SqlDialect sqlDialect; // Dialect Integration

    public Subquery(CriteriaQuery<?> criteria, SqlDialect sqlDialect) { // Dialect Integration - constructor now accepts SqlDialect
        this.cr = criteria;
        this.sqlDialect = sqlDialect;
    }

    public CriteriaQuery<?> cr() {
        return cr;
    }

    @Override
    public String toSql() {
        return " (" + cr.buildSelectQuery() + ") ";
    }

    @Override
    public Selectable get(String attr) {
        return new Path<>(alias, attr);
    }

    // No changes needed for getName() and setAlias() as they are dialect-independent

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }
}