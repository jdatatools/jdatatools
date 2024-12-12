package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Starts with operator
 */
public class StartsWith extends Expression {

    public StartsWith(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }


    public String sql() {
        return " like '" + value + "%'";
    }
}
