package com.ainouss.datatools.jdatatools.query.core;

public abstract class AggregateFunction implements Aggregable {

    protected final Selectable selectable;

    public AggregateFunction(Selectable selectable) {
        this.selectable = selectable;
    }

    @Override
    public Selectable getColumn() {
        return selectable;
    }

    @Override
    public String column() {
        return selectable.column();
    }

    @Override
    public Root<?> root() {
        return selectable.root();
    }
}
