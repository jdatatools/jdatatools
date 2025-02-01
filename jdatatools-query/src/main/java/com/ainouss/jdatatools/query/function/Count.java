package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Root;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

/**
 * Avg aggregate function
 */
public class Count extends AggregateFunction {

    private final SqlDialect sqlDialect; // Dialect Integration

    public Count(Selectable selectable, SqlDialect sqlDialect) { // Dialect Integration
        super(selectable, sqlDialect);
        this.sqlDialect = sqlDialect;
    }

    public Count(SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect);
        this.sqlDialect = sqlDialect;
    }


    @Override
    public String sql() {
        if (selectable == null || selectable instanceof Root<?>) {
            return "count(*)";
        }
        return "count(" + selectable.toSql() + ")";
    }


}