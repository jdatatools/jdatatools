package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

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


    public String toSql() {
        String escaped = right.toSql().replace("'", "");
        return attribute.toSql() + " like '" + escaped + "%'";
    }

}
