package com.ainouss.jdatatools.query.setoperation;

import com.ainouss.jdatatools.query.core.CriteriaQuery;

public class Intersect implements SetOperation {

    private final CriteriaQuery<?> other;

    public Intersect(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "intersect (" + other.buildSelectQuery() + ")";
    }

}
