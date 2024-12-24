package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Sum extends AggregateFunction {


    public Sum(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        return "sum(" + selectable.toSql() + ")";
    }

}
