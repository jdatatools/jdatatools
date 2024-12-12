package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selection;

/**
 * Count aggregate function
 */
public class Count extends Expression {

    public Count(Selection<?> path) {
        this.value = path;
    }

    public Count() {
    }

    @Override
    protected String sql() {
        if (value == null) {
            return "count(*)";
        }
        return "count(" + value + ")";
    }
}
