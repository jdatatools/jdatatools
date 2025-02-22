package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Avg aggregate function
 */
public class Avg extends AggregateFunction {

    public Avg(Selectable selectable, SqlDialect sqlDialect) {
        super(selectable, sqlDialect);
    }

    @Override
    public String sql() {
        return "avg(" + selectable.toSql() + ")";
    }

}