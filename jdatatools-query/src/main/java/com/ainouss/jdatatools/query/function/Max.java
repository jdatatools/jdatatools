package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * Avg aggregate function
 */
public class Max extends AggregateFunction {

    private final SqlDialect sqlDialect;

    public Max(Selectable selectable, SqlDialect sqlDialect) {
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;// Dialect Integration
    }

    @Override
    public String sql() {
        return "max(" + selectable.toSql() + ")";
    }

}