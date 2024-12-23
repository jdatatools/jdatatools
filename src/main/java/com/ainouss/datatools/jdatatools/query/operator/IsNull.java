package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * A field with a NULL value is a field with no value.
 */
public class IsNull extends Expression {

    private final Selectable attribute;

    public IsNull(Selectable attribute) {
        this.attribute = attribute;
    }

    public String sql() {
        return attribute.toSql() + " is null ";
    }
}
