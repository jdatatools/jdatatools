package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * Avg aggregate function
 */
public class Distinct extends AggregateFunction {

    private final SqlDialect sqlDialect;

    public Distinct(Selectable selectable, SqlDialect sqlDialect) {
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;
    }

    @Override
    public String sql() {
        return sqlDialect.getDistinctKeyword() + " " + selectable.toSql();
    }

}