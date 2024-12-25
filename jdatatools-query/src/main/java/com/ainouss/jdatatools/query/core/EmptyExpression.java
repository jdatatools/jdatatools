package com.ainouss.jdatatools.query.core;

/**
 * Identity expression, renders an empty SQL
 */
public class EmptyExpression implements Expression {

    public EmptyExpression() {
    }

    @Override
    public String toSql() {
        return "";
    }
}
