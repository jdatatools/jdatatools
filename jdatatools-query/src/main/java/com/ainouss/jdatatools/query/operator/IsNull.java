package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * A field with a NULL value is a field with no value.
 */
public class IsNull implements Expression {

    private final Selectable attribute;

    public IsNull(Selectable attribute) {
        this.attribute = attribute;
    }

    public String toSql() {
        return attribute.toSql() + " is null ";
    }
}
