package com.ainouss.datatools.jdatatools.query.core;

public abstract class NoAlias implements WithAlias {

    @Override
    public void setAlias(String alias) {
    }

    @Override
    public String getAlias() {
        return "";
    }
}
