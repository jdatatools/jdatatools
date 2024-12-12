package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

/**
 * Greater Than operator, denoted as ">", is used to compare values in a database table
 * and retrieve rows where a specific column's value is greater than a given criteria.
 */
public class Gt extends Expression {

    public Gt(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }


    public String sql() {
        if (value == null) {
            throw new RuntimeException("Greater than operator should be used with a single expression or a non null value.");
        }
        if (value instanceof String) {
            return " > '" + value + "'";
        }
        return " > " + value;
    }
}
