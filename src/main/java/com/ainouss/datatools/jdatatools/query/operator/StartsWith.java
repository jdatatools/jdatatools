package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

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
