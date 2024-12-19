package com.ainouss.datatools.jdatatools.query.core;

public class StaticPath implements Selectable {

    private final String path;
    public String alias;

    public StaticPath(String path) {
        this.path = path;
    }

    @Override
    public String toSql() {
        return path;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }
}
