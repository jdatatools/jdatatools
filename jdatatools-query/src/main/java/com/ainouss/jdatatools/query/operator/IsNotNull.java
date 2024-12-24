package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Is not null expression
 */
public class IsNotNull extends Expression {

    private final Selectable attribute;

    public IsNotNull(Selectable attribute) {
        this.attribute = attribute;
    }

    public String sql() {
        return attribute.toSql() + " is not null ";
    }
}
