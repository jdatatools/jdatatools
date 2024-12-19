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
        } else if (value instanceof Subquery su) {
            return su.render() + " " + su.getAlias();
        } else if (value instanceof CriteriaQuery<?> cr) {
            return " (" + cr.buildSelectQuery() + ")";
        } else if (value instanceof Expression ex) {
            return ex.render();
        }
        return value.toString();
    }

    @Override
    public void setAlias(String alias) {

    }

    @Override
    public String getAlias() {
        return "";
    }
}
