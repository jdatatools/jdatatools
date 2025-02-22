package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Starts with operator
 */
public class StartsWith implements Expression {

    private final Selectable attribute;
    private final Selectable right;
    private final SqlDialect sqlDialect;

    public StartsWith(Selectable attribute, Selectable right, SqlDialect sqlDialect) {
        this.attribute = attribute;
        this.right = right;
        this.sqlDialect = sqlDialect;
    }


    public String toSql() {
        String escaped = right.toSql().replace("'", "");
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " like '" + escaped + "%'";
    }

}