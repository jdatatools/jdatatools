package com.ainouss.jdatatools.query.function;

public class Rank extends AggregateFunction {

    public Rank() {
        super();
    }

    @Override
    public String sql() {
        return "rank()";
    }
}
