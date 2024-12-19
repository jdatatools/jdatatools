package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.util.DataUtils;

public class Subquery implements Source {

    private final CriteriaQuery<?> cr;
    private String alias;

    public Subquery(CriteriaQuery<?> criteria) {
        this.cr = criteria;
    }

    public CriteriaQuery<?> cr() {
        return cr;
    }

    @Override
    public String render() {
        return " (" + cr.buildSelectQuery() + ") ";
    }

    @Override
    public String getAlias() {
        return DataUtils.trimToBlank(alias);
    }


    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }
}