package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

/**
 * Starts with operator
 */
public class EndsWith extends Expression {

    public EndsWith(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }


    public String sql() {
        if (value instanceof Path<?>) {
            return " like '%' +" + value;
        }
        return " like '%" + value + "'";
    }
}
