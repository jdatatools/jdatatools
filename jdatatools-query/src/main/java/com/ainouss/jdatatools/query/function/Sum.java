package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Avg aggregate function
 */
public class Sum extends AggregateFunction {

    private final SqlDialect sqlDialect;

    public Sum(Selectable selectable, SqlDialect sqlDialect) {
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;// Dialect Integration
    }

    @Override
    public String sql() {
        return "sum(" + selectable.toSql() + ")";
    }

}