package com.ainouss.datatools.jdatatools.query.subquery;


import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Root;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

public class All extends Expression implements Selectable {

    private final CriteriaQuery<?> subquery; // Subquery is now a local variable

    public All(CriteriaQuery<?> subquery) {
        this.subquery = subquery;
    }

    @Override
    public String column() {
        return "";
    }

    @Override
    public Root<?> root() {
        return subquery.from();
    }

    @Override
    public String toSql() {
        return " all (" + subquery.buildSelectQuery() + ")";
    }
}