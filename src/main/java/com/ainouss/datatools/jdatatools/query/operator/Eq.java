package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Expression;

import static org.springframework.util.ObjectUtils.isArray;

/**
 * Equal clause, accepts numbers, paths and strings
 */
public class Eq extends Expression {

    public Eq(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    public String sql() {
        if (value == null || isArray(value)) {
            throw new RuntimeException("Equal should be used with a single non null value");
        }
        var sql = " = ";
        if (value instanceof Number || value instanceof Path<?>) {
            return sql + value;
        }
        return sql + "'" + value + "'";
    }
}
