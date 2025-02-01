package com.ainouss.jdatatools.query.setoperation;

import com.ainouss.jdatatools.query.core.CriteriaQuery;

public class UnionAll implements SetOperation {

    private final CriteriaQuery<?> other;

    public UnionAll(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "union all (" + other.buildSelectQuery() + ")";
    }

}