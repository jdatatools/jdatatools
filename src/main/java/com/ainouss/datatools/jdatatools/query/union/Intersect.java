package com.ainouss.datatools.jdatatools.query.union;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.SetOperation;

public class Intersect implements SetOperation {

    private final CriteriaQuery<?> other;

    public Intersect(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "intersect " + other.buildSelectQuery();
    }

}
