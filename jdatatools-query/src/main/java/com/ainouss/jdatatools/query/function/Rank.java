package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.dialect.SqlDialect;

public class Rank extends AggregateFunction {

    private final SqlDialect sqlDialect;
    public Rank(SqlDialect sqlDialect) {
        super(sqlDialect);
        this.sqlDialect = sqlDialect;
    }

    public Rank() { // For CTE - default constructor
        this(null);
    }


    @Override
    public String sql() {
        return "rank()";
    }
}