package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Avg aggregate function
 */
public class Max extends AggregateFunction {

    private final SqlDialect sqlDialect; // Dialect Integration

    public Max(Selectable selectable, SqlDialect sqlDialect) { // Dialect Integration
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;// Dialect Integration
    }

    @Override
    public String sql() {
        return "max(" + selectable.toSql() + ")";
    }

}