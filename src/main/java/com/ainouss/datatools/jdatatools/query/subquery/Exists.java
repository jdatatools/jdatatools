package com.ainouss.datatools.jdatatools.query.subquery;


import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;

public class Exists extends Expression {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public Exists(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
    }

    @Override
    protected String sql() {
        return " exists (" + subquery.buildSelectQuery() + ")";
    }

    @Override
    public String toString() {
        return sql();
    }
}