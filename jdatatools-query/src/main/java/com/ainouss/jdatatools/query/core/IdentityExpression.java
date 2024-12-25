package com.ainouss.jdatatools.query.core;

/**
 * Identity expression, renders an empty SQL
 */
public class IdentityExpression implements Expression {

    public IdentityExpression() {
    }

    @Override
    public String toSql() {
        return "";
    }
}
