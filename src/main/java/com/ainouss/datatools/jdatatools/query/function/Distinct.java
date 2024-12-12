package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selection;

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
