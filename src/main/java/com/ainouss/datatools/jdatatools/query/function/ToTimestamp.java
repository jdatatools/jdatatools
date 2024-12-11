package com.ainouss.datatools.jdatatools.query.function;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

public class ToTimestamp extends Expression {
    private String dateFormat = "YYYYMMDD HH:MI:SS";

    public ToTimestamp(Path<?> path) {
        this.value = path;
    }

    public ToTimestamp(Path<?> path, String dateFormat) {
        this.value = path;
        this.dateFormat = dateFormat;
    }

    @Override
    protected String sql() {
        return "TO_TIMESTAMP(%s,'%s')".formatted(value, dateFormat);
    }
}
