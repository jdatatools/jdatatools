package com.ainouss.jdatatools.query.union;

import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.SetOperation;

public class UnionAll implements SetOperation {

    private final CriteriaQuery<?> other;

    public UnionAll(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "union all (" + other.buildSelectQuery() + ")";
    }

}
