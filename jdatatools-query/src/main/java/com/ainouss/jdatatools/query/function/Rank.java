package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.AggregateFunction;

public class Rank extends AggregateFunction {

    public Rank() {
        super();
    }

    @Override
    public String sql() {
        return "rank()";
    }
}
