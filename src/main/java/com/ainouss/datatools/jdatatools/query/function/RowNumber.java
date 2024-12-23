package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;

public class RowNumber extends AggregateFunction {

    public RowNumber() {
        super();
    }

    @Override
    public String sql() {
        return "row_number()";
    }
}
