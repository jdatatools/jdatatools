package com.ainouss.datatools.jdatatools.query.expression;

import com.ainouss.datatools.jdatatools.query.Expression;

/**
 * Identity expression, renders an empty sql
 */
public class IdentityExpression extends Expression {

    public IdentityExpression() {
    }

    @Override
    protected String sql() {
        return "";
    }
}
