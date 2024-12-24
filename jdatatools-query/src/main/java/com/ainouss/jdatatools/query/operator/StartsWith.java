package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Starts with operator
 */
public class StartsWith extends Expression {

    private final Selectable attribute;
    private final Selectable right;

    public StartsWith(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }


    public String sql() {
        String escaped = right.toSql().replace("'", "");
        return attribute.toSql() + " like '" + escaped + "%'";
    }

}
