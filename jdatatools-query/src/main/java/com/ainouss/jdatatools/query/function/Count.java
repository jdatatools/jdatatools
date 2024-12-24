package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.AggregateFunction;
import com.ainouss.jdatatools.query.core.Root;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Avg aggregate function
 */
public class Count extends AggregateFunction {


    public Count(Selectable selectable) {
        super(selectable);
    }

    @Override
    public String sql() {
        if (selectable == null || selectable instanceof Root<?>) {
            return "count(*)";
        }
        return "count(" + selectable.toSql() + ")";
    }


}
