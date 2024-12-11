package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.Expression;

/**
 * The WHERE clause can contain one or many AND operators.
 * The AND operator is used to filter records based on more than one condition,
 */
public class And extends Expression {

    public And(Expression expression) {
        this.and.add(expression);
    }

    @Override
    protected String sql() {
        return "";
    }
}
