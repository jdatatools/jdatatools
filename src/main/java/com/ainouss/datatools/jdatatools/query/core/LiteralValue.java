package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.util.DataUtils;

public class LiteralValue extends Alias implements Selectable {

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
        } else if (value instanceof Fragment fr) {
            return fr.toSql();
        }
        return value.toString();
    }
}
