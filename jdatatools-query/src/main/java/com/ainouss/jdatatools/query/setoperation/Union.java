package com.ainouss.jdatatools.query.setoperation;

import com.ainouss.jdatatools.query.core.CriteriaQuery;

public class Union implements SetOperation {

    private final CriteriaQuery<?> other;

    public Union(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "union (" + other.buildSelectQuery()+ ")";
    }


}
