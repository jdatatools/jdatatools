package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.util.DataUtils;

public abstract class Alias implements WithAlias {

    protected String alias;

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return DataUtils.trimToBlank(alias);
    }
}
