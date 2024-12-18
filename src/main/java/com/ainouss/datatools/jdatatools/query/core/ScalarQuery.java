package com.ainouss.datatools.jdatatools.query.core;

public class ScalarQuery<T> implements Selectable {

    private final CriteriaQuery<T> criteria;

    public ScalarQuery(CriteriaQuery<T> criteria) {
        this.criteria = criteria;
    }


    @Override
    public String toSql() {
        return " (" + criteria.buildSelectQuery() + ")";
    }

    @Override
    public String column() {
        return criteria.getSelect().stream().findFirst().map(Selectable::column).orElse(null);
    }

    @Override
    public Root<T> root() {
        return criteria.from();
    }
}
