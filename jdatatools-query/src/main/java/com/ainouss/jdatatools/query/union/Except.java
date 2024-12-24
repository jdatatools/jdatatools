package com.ainouss.jdatatools.query.union;

import com.ainouss.jdatatools.query.core.CriteriaQuery;
import com.ainouss.jdatatools.query.core.SetOperation;

public class Except implements SetOperation {

    private final CriteriaQuery<?> other;

    public Except(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "except (" + other.buildSelectQuery() + ")";
    }

}
