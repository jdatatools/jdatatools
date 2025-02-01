package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

/**
 * Avg aggregate function
 */
public class Distinct extends AggregateFunction {

    private final SqlDialect sqlDialect; // Dialect Integration

    public Distinct(Selectable selectable, SqlDialect sqlDialect) { // Dialect Integration
        super(selectable, sqlDialect); // Dialect Integration
        this.sqlDialect = sqlDialect;
    }

    @Override
    public String sql() {
        return sqlDialect.getDistinctKeyword() + " " + selectable.toSql(); // Dialect Integration
    }

}