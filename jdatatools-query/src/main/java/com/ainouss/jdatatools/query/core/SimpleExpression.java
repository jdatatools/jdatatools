package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Where expression, entry point to where conditions
 */
public class SimpleExpression implements Expression {

    private Expression expression;
    private final SqlDialect sqlDialect;

    public SimpleExpression(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    public SimpleExpression() {
        this(null); // For CTE - default constructor
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