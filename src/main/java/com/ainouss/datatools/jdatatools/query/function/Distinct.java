package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.PathExpression;

/**
 * Distinct aggregate function
 */
public class Distinct extends Expression {
    public Distinct(PathExpression<?> path) {
        this.value = path;
    }

    @Override
    protected String sql() {
        return "distinct " + value;
    }
}
