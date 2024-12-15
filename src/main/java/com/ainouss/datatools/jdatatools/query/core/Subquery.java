package com.ainouss.datatools.jdatatools.query.core;

public class Subquery<T> extends CriteriaQuery<T> implements From {

    private final CriteriaQuery<?> parent;// TODO : overload alias

    public Subquery(CriteriaQuery<?> cr, Class<T> type) {
        super(type);
        this.parent = cr;
        this.subquery = true;
    }

    @Override
    public String render() {
        return " (" + buildSelectQuery() + ") " + getAlias();
    }

    @Override
    public Subquery<T> as(String tbl) {
        return this;
    }

    @Override
    public String getAlias() {
        return this.from().getAlias();
    }
}