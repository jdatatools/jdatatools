package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.util.DataUtils;

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
