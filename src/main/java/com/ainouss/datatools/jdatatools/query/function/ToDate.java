package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;


public class ToDate extends Expression {
    private String dateFormat = "YYYYMMDD HH:MI:SS";

    public ToDate(Path<?> path) {
        this.value = path;
    }

    public ToDate(Path<?> path, String dateFormat) {
        this.value = path;
        this.dateFormat = dateFormat;
    }

    @Override
    protected String sql() {
        return "TO_DATE(%s,'%s')".formatted(value, dateFormat);
    }
}
