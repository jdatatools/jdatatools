package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.SqlDialect;

public class RowNumber extends AggregateFunction {

    private final SqlDialect sqlDialect;

    public RowNumber(SqlDialect sqlDialect) {
        super(sqlDialect);
        this.sqlDialect = sqlDialect;
    }

    public RowNumber() { // For CTE - default constructor
        this(null);
    }


    @Override
    public String sql() {
        return "row_number()";
    }
}