package com.ainouss.jdatatools.query.core;

public interface Selectable extends Expression, WithAlias, Comparable<Selectable> {

    default Selectable as(String alias) {
        setAlias(alias);
        return this;
    }

    @Override
    default int compareTo(Selectable o) {
        String alias = getAlias();
        if (alias == null) {
            return 0;
        }
        return alias.compareTo(o.getAlias());
    }
}
