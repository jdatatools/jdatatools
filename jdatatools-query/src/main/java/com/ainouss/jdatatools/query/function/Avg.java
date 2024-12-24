package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Avg extends AggregateFunction {

    public Avg(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        return "avg(" + selectable.toSql() + ")";
    }

}
