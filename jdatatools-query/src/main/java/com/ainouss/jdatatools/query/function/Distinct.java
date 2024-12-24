package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Distinct extends AggregateFunction {


    public Distinct(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        return "distinct " + selectable.toSql();
    }

}
