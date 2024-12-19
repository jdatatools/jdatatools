package com.ainouss.datatools.jdatatools.query.core;

public class ScalarQuery<T> implements Selectable {

    private final CriteriaQuery<T> criteria;
    private String alias;

    public ScalarQuery(CriteriaQuery<T> criteria) {
        this.criteria = criteria;
        this.alias = criteria.getSelect().stream().findAny().map(Selectable::getAlias).orElse("");
    }


    @Override
    public String toSql() {
        return " (" + criteria.buildSelectQuery() + ")";
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }
}
