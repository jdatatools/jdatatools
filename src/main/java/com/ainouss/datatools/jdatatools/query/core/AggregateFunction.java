package com.ainouss.datatools.jdatatools.query.core;

public abstract class AggregateFunction extends Alias implements Aggregable {

    protected final Selectable selectable;


    public AggregateFunction(Selectable selectable) {
        this.selectable = selectable;
    }
}
