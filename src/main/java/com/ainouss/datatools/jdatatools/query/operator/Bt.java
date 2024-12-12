package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Expression;

/**
 * Between clause, accepts numbers and paths
 */
public class Bt extends Expression {
    private final Object value1;
    private final Object value2;

    public Bt(Path<?> path, Object value1, Object value2) {
        this.path = path;
        this.value1 = value1;
        this.value2 = value2;
        this.value = new Object[]{value1, value2};
    }

    public String sql() {
        if (value == null || value1 == null || value2 == null) {
            throw new RuntimeException("Between operator should be used with a two non null values");
        }
        return " between " + value1 + " and " + value2;
    }
}
