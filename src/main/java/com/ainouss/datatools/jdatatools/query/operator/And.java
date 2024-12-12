package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;

/**
 * The WHERE clause can contain one or many AND operators.
 * The 'AND' operator is used to filter items based on more than one condition
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
