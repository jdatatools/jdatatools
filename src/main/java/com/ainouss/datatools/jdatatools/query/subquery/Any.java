package com.ainouss.datatools.jdatatools.query.subquery;


import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;

public class Any extends Expression {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public Any(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
    }

    @Override
    public String toString() {
        return " any (" + subquery.buildSelectQuery() + ")";
    }
}