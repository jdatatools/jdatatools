package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Is not null expression
 */
public class IsNotNull implements Expression {

    private final Selectable attribute;

    public IsNotNull(Selectable attribute) {
        this.attribute = attribute;
    }

    public String toSql() {
        return attribute.toSql() + " is not null ";
    }
}
