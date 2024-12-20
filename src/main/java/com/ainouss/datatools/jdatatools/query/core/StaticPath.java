package com.ainouss.datatools.jdatatools.query.core;

public class StaticPath extends Alias implements Selectable {

    private final String path;
    public String alias;

    public StaticPath(String path) {
        this.path = path;
    }

    @Override
    public String toSql() {
        return path;
    }
}
