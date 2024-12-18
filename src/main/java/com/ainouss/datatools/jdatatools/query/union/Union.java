package com.ainouss.datatools.jdatatools.query.union;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.SetOperation;

public class Union implements SetOperation {

    private final CriteriaQuery<?> other;

    public Union(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "union (" + other.buildSelectQuery()+ ")";
    }


}
