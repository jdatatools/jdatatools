package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class Rank extends AggregateFunction {

    private final SqlDialect sqlDialect; // Dialect Integration
    public Rank(SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect); // Dialect Integration
        this.sqlDialect = sqlDialect;
    }

    public Rank() { // For CTE - default constructor // Dialect Integration
        this(null);
    }


    @Override
    public String sql() {
        return "rank()";
    }
}