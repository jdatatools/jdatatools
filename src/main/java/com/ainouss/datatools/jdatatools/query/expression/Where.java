package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.core.Expression;

/**
 * Where expression, entry point to where conditions
 */
public class Where extends Expression {

    private  Expression expression;

    public Where() {
    }

    public void with(Expression expression) {
        this.expression = expression;
    }

    public String sql() {
        if (expression == null) {
            return "";
        }
        return expression.toSql();
    }
}
