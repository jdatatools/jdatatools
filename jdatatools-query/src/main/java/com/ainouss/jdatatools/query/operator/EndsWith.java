package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Starts with operator
 */
public class EndsWith implements Expression {

    private final Selectable attribute;
    private final Selectable right;

    public EndsWith(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    public String toSql() {
        String escaped = right.toSql().replace("'", "");
        return attribute.toSql() + " like '%" + escaped + "'";
    }
}
