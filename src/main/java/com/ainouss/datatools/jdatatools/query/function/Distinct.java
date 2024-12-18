package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Distinct extends AggregateFunction {


    public Distinct(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String toSql() {
        return "distinct " + selectable.toSql();
    }

}
