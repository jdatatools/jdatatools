package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;

/**
 * The NOT operator is used in combination with other operators to give the opposite result, also called the negative result.
 */
public class Not extends Expression {

    public Not(Expression expression) {
        this.not.add(expression);
    }

    public String sql() {
        return " NOT ";
    }
}
