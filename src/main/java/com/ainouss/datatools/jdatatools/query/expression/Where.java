package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.core.Expression;

/**
 * Where expression, entry point to where conditions
 */
public class Where extends Expression {

    public Where() {
    }

    public Where(Expression expression) {
        this.expression = expression;
    }

    @Override
    protected String sql() {
        if (expression == null) {
            return "";
        }
        return " where ";
    }
}
