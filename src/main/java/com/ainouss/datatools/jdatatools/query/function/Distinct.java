package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Selection;

/**
 * Distinct aggregate function
 */
public class Distinct extends Expression {
    public Distinct(Selection<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "distinct " + value;
    }
}
