package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;

public class Rank extends AggregateFunction {

    public Rank() {
        super();
    }

    @Override
    public String sql() {
        return "rank()";
    }
}
