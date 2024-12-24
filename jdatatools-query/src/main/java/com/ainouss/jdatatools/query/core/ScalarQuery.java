package com.ainouss.jdatatools.query.core;

public class ScalarQuery<T> extends Alias implements Selectable {

    private final CriteriaQuery<T> criteria;

    public ScalarQuery(CriteriaQuery<T> criteria) {
        this.criteria = criteria;
        this.alias = criteria.getSelect().stream()
                .findAny()
                .map(Selectable::getAlias)
                .orElse("");
    }


    @Override
    public String toSql() {
        return " (" + criteria.buildSelectQuery() + ")";
    }
}
