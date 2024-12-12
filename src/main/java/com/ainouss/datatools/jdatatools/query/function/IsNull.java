package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * A field with a NULL value is a field with no value.
 */
public class IsNull extends Expression {

    public IsNull(Path<?> root) {
        this.path = root;
    }

    public String sql() {
        return " is null ";
    }
}
