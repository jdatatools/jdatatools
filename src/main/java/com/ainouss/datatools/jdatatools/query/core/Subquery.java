package com.ainouss.datatools.jdatatools.query.core;

public class Subquery implements Source {

    private final CriteriaQuery<?> cr;

    public Subquery(CriteriaQuery<?> criteria) {
        this.cr = criteria;
        criteria.noAlias = true;
    }

    @Override
    public String render() {
        return " (" + cr.buildSelectQuery() + ") ";
    }

    @Override
    public String getAlias() {
        return this.cr.getRoot().getAlias();
    }

    public CriteriaQuery<?> cr() {
        return cr;
    }
}