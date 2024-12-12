package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Like operator
 */
public class Like extends Expression {

    public Like(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }


    public String sql() {
        return " like '%" + value + "%'";
    }
}
