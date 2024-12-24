package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.AggregateFunction;

public class RowNumber extends AggregateFunction {

    public RowNumber() {
        super();
    }

    @Override
    public String sql() {
        return "row_number()";
    }
}
