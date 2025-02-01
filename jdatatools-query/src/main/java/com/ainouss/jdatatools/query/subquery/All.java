package com.ainouss.jdatatools.query.subquery;


import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

public class All implements Selectable {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable
    private final SqlDialect sqlDialect; // Dialect Integration

    public All(CriteriaQuery<?> subquery, SqlDialect sqlDialect) { // Dialect Integration
        this.subquery = subquery;
        this.sqlDialect = sqlDialect;
    }

    public All(CriteriaQuery<?> subquery) { // For CriteriaBuilder default constructor // Dialect Integration
        this(subquery, null);
    }


    @Override
    public String toSql() {
        return " all (" + subquery.buildSelectQuery() + ")";
    }

    @Override
    public void setAlias(String alias) {
        //
    }

    @Override
    public String getAlias() {
        return "";
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }
}