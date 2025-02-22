package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.dialect.SqlDialect;

public class Subquery extends Alias implements Source {

    private final CriteriaQuery<?> cr;
    private final SqlDialect sqlDialect;

    public Subquery(CriteriaQuery<?> criteria, SqlDialect sqlDialect) {
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


    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }
}