package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Sum aggregate function
 */
public class Sum extends Expression {
    public Sum(Path<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "sum(" + value + ")";
    }
}
