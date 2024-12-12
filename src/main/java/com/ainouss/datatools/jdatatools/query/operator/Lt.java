package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Lesser than operator, accepts numbers and expressions
 */
public class Lt extends Expression {

    public Lt(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String sql() {

        if (value instanceof String) {
            return " < '" + value + "'";
        }
        return " < " + value;

    }
}
