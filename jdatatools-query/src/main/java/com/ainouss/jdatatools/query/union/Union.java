package com.ainouss.jdatatools.query.union;

import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.SetOperation;

public class Union implements SetOperation {

    private final CriteriaQuery<?> other;

    public Union(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "union (" + other.buildSelectQuery()+ ")";
    }


}
