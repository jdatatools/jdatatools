package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.util.DataUtils;

public interface Selectable extends Expression, WithAlias, Comparable<Selectable> {

    default String getColumn() {
        String sql = toSql();
        if (DataUtils.isBlank(sql)) {
            return "";
        }
        if (sql.contains(".")) {
            return sql.substring(sql.lastIndexOf(".") + 1);
        }
        return sql;
    }

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
