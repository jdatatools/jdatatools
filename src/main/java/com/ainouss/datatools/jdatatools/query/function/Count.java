package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.AggregateFunction;
import com.ainouss.datatools.jdatatools.query.core.Root;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Count extends AggregateFunction {


    public Count(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String toString() {
        if (selectable == null || selectable instanceof Root<?>) {
            return "count(*)";
        }
        return "count(" + selectable + ")";
    }

}
