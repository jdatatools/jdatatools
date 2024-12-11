package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

/**
 * Min aggregate function
 */
public class Min extends Expression {
    public Min(Path<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "min(" + value + ")";
    }
}
