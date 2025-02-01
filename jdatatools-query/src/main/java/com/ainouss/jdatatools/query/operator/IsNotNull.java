package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

/**
 * Is not null expression
 */
public class IsNotNull implements Expression {

    private final Selectable attribute;
    private final SqlDialect sqlDialect; // Dialect Integration

    public IsNotNull(Selectable attribute, SqlDialect sqlDialect) { // Dialect Integration
        this.attribute = attribute;
        this.sqlDialect = sqlDialect;
    }

    public String toSql() {
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " is not null "; // Dialect Integration
    }
}