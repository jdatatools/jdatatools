package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

/**
 * Avg aggregate function
 */
public class Avg extends AggregateFunction {

    public Avg(Selectable selectable, SqlDialect sqlDialect) { // Dialect Integration
        super(selectable, sqlDialect); // Dialect Integration
    }

    @Override
    public String sql() {
        return "avg(" + selectable.toSql() + ")";
    }

}