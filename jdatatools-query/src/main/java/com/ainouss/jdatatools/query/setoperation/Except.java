package com.ainouss.jdatatools.query.setoperation;

import com.ainouss.jdatatools.query.core.CriteriaQuery;

public class Except implements SetOperation {

    private final CriteriaQuery<?> other;

    public Except(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "except (" + other.buildSelectQuery() + ")";
    }

}
