package com.ainouss.jdatatools.query.function;

public class RowNumber extends AggregateFunction {

    public RowNumber() {
        super();
    }

    @Override
    public String sql() {
        return "row_number()";
    }
}
