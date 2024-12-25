package com.ainouss.jdatatools.query.subquery;


import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.Selectable;

public class All implements Selectable {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public All(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
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
}