package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Avg aggregate function
 */
public class Min extends AggregateFunction {

    private final SqlDialect sqlDialect;

    public Min(Selectable selectable, SqlDialect sqlDialect) {
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;
    }

    @Override
    public String sql() {
        return "min(" + selectable.toSql() + ")";
    }

}