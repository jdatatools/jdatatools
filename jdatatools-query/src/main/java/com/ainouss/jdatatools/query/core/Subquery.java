package com.ainouss.jdatatools.query.core;

public class Subquery extends Alias implements Source {

    private final CriteriaQuery<?> cr;

    public Subquery(CriteriaQuery<?> criteria) {
        this.cr = criteria;
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
}