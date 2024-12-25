package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.AbstractExpression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Starts with operator
 */
public class EndsWith extends AbstractExpression {

    private final Selectable attribute;
    private final Selectable right;

    public EndsWith(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    public String sql() {
        String escaped = right.toSql().replace("'", "");
        return attribute.toSql() + " like '%" + escaped + "'";
    }
}
