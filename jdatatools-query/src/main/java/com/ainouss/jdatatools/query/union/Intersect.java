package com.ainouss.jdatatools.query.union;

import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.SetOperation;

public class Intersect implements SetOperation {

    private final CriteriaQuery<?> other;

    public Intersect(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "intersect (" + other.buildSelectQuery() + ")";
    }

}
