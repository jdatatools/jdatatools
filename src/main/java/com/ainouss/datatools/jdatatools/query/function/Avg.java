package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Avg aggregate function
 */
public class Avg extends Expression {
    public Avg(Path<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "avg(" + value + ")";
    }
}
