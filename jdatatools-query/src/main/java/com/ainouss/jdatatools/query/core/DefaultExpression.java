package com.ainouss.jdatatools.query.core;

/**
 * Where expression, entry point to where conditions
 */
public class DefaultExpression implements Expression {

    private Expression expression;

    public DefaultExpression() {
    }

    public void with(Expression expression) {
        this.expression = expression;
    }

    public String toSql() {
        if (expression == null) {
            return "";
        }
        return expression.toSql();
    }
}
