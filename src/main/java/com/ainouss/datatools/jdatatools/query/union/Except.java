package com.ainouss.datatools.jdatatools.query.union;

import com.ainouss.datatools.jdatatools.query.core.CriteriaQuery;
import com.ainouss.datatools.jdatatools.query.core.SetOperation;

public class Except implements SetOperation {

    private final CriteriaQuery<?> other;

    public Except(CriteriaQuery<?> other) {
        this.other = other;
    }

    public String toSql() {
        return "except (" + other.buildSelectQuery() + ")";
    }

}
