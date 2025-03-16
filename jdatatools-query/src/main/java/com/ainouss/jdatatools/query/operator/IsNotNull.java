package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * Is not null expression
 */
public class IsNotNull implements Expression {

    private final Selectable attribute;
    private final SqlDialect sqlDialect;

    public IsNotNull(Selectable attribute, SqlDialect sqlDialect) {
        this.attribute = attribute;
        this.sqlDialect = sqlDialect;
    }

    public String toSql() {
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " is not null ";
    }
}