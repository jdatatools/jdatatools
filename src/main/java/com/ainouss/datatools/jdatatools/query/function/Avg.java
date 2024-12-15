package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Avg extends AggregateFunction {


    public Avg(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String toString() {
        return "avg(" + selectable.toString() + ")";
    }

}
