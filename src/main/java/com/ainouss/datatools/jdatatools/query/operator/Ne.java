package com.ainouss.datatools.jdatatools.query.operator;


import com.ainouss.datatools.jdatatools.query.Path;
import com.ainouss.datatools.jdatatools.query.Expression;

import static org.springframework.util.ObjectUtils.isArray;

/**
 * Not Equal clause, accepts numbers, paths and strings
 */
public class Ne extends Expression {

    public Ne(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String sql() {
        if (value == null || isArray(value)) {
            throw new RuntimeException("Not equal should be used with a single non null value");
        }
        var sql = " != ";
        if (value instanceof Number || value instanceof Path<?>) {
            return sql + value;
        }
        return sql + "'" + value + "'";
    }
}