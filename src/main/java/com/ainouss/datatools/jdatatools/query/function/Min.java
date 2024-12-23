package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Min extends AggregateFunction {


    public Min(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        return "min(" + selectable.toSql() + ")";
    }

}
