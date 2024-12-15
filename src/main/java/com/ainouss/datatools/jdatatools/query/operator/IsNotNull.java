package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Is not null expression
 */
public class IsNotNull extends Expression {

    private final Selectable attribute;

    public IsNotNull(Selectable attribute) {
        this.attribute = attribute;
    }

    public String toString() {
        return attribute + " is not null ";
    }
}
