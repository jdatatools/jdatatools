package com.ainouss.datatools.jdatatools.query.subquery;


import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;

public class All extends Expression {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public All(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
    }

    @Override
    protected String sql() {
        return " all (" + subquery.buildSelectQuery() + ")";
    }

    @Override
    public String toString() {
        return sql();
    }
}