package com.ainouss.jdatatools.query.subquery;


import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

public class Exists extends Expression implements Selectable {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public Exists(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
    }

    @Override
    public String sql() {
        return " exists (" + subquery.buildSelectQuery() + ")";
    }

    @Override
    public void setAlias(String alias) {
        //
    }

    @Override
    public String getAlias() {
        return "";
    }
}