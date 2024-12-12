package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.core.Expression;

/**
 * Where expression, entry point to where conditions
 */
public class Where extends Expression {

    public Where(Expression expression) {
        this.and.add(expression);
    }

    @Override
    protected String sql() {
        return "";
    }
}
