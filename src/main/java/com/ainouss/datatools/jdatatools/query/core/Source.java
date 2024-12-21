package com.ainouss.datatools.jdatatools.query.core;

public interface Source extends Fragment, WithAlias {

    Selectable get(String attr);

    default String getName() {
        return toSql();
    }

    String getAlias();

    void setAlias(String alias);

}
