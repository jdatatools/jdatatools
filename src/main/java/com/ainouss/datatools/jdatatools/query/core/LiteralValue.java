package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.util.DataUtils;

class LiteralValue implements Selectable {

    private final Object value;

    public LiteralValue(Object value) {
        this.value = value;
    }

    @Override
    public String toSql() {
        if (value == null) {
            return "";
        }
        if (value instanceof String strValue) {
            return "'" + DataUtils.escapeSql(strValue) + "'";
        } else if (value instanceof Number) {
            return value.toString();
        }
        return value.toString();
    }

    @Override
    public String column() {
        return "";
    }

    @Override
    public Root<?> root() {
        return null;
    }
}
