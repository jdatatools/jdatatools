package com.ainouss.datatools.jdatatools.query.core;

public interface Source extends Fragment, WithAlias {

    default String render() {
        return toSql();
    }

    String getAlias();

    void setAlias(String alias);

}
