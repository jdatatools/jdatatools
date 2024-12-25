package com.ainouss.jdatatools.query.core;

/**
 * Where expression, entry point to where conditions
 */
public class Where extends AbstractExpression {

    private AbstractExpression expression;

    public Where() {
    }

    public void with(AbstractExpression expression) {
        this.expression = expression;
    }

    public String sql() {
        if (expression == null) {
            return "";
        }
        return expression.toSql();
    }
}
