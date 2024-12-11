package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.Expression;

/**
 * The WHERE clause can contain one or more OR operators.
 * <p>
 * The OR operator is used to filter records based on more than one condition
 */
public class Or extends Expression {

    public Or(Expression expression) {
        this.or.add(expression);
    }

    @Override
    protected String sql() {
        return "";
    }
}
