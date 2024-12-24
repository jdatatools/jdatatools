package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Max extends AggregateFunction {


    public Max(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        return "max(" + selectable.toSql() + ")";
    }

}
