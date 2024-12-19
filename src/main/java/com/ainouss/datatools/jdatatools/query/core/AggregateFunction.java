package com.ainouss.datatools.jdatatools.query.core;

public abstract class AggregateFunction implements Aggregable {

    protected final Selectable selectable;
    protected String alias;


    public AggregateFunction(Selectable selectable) {
        this.selectable = selectable;
    }

    @Override
    public Selectable getColumn() {
        return selectable;
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
