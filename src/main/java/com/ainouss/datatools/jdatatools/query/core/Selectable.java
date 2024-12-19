package com.ainouss.datatools.jdatatools.query.core;

public interface Selectable extends Comparable<Selectable> {

    String toSql();

    void setAlias(String alias);

    String getAlias();

    @Override
    default int compareTo(Selectable o) {
        String alias = getAlias();
        if (alias == null) {
            return 0;
        }
        return alias.compareTo(o.getAlias());
    }
}
