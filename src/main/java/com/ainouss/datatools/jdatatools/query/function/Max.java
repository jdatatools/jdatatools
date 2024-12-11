package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

/**
 * Max aggregate function
 */
public class Max extends Expression {
    public Max(Path<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "max(" + value + ")";
    }
}
