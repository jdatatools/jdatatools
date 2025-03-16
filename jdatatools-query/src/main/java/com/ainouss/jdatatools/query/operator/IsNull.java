package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * A field with a NULL value is a field with no value.
 */
public class IsNull implements Expression {

    private final Selectable attribute;
    private final SqlDialect sqlDialect;

    public IsNull(Selectable attribute, SqlDialect sqlDialect) {
        this.attribute = attribute;
        this.sqlDialect = sqlDialect;
    }

    public String toSql() {
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " is null ";
    }
}