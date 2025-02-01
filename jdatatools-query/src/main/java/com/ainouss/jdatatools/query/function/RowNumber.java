package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class RowNumber extends AggregateFunction {

    private final SqlDialect sqlDialect; // Dialect Integration

    public RowNumber(SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect); // Dialect Integration
        this.sqlDialect = sqlDialect;
    }

    public RowNumber() { // For CTE - default constructor // Dialect Integration
        this(null);
    }


    @Override
    public String sql() {
        return "row_number()";
    }
}