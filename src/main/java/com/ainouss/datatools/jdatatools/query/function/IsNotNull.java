package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Is not null expression
 */
public class IsNotNull extends Expression {


    public IsNotNull(Path<?> path) {
        this.path = path;
    }

    public String sql() {
        return " is not null ";
    }
}
