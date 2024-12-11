package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lesser than operator, accepts numbers and expressions
 */
public class Lt extends Expression {
    private String sqlDateFormat = "YYYYMMDD HH:MI:SS";
    private String df = "yyyyMMdd hh:mm:ss";
    final SimpleDateFormat sdf = new SimpleDateFormat(df);

    public Lt(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    public <R> Lt(Path<R> date, LocalDateTime localDateTime, String dateFormat) {
        this.path = date;
        this.value = localDateTime;
        this.sqlDateFormat = dateFormat;
    }

    public String sql() {
        if (value instanceof LocalDateTime ldt) {
            return " < TO_DATE('%s', '%s')".formatted(sdf.format(ldt), sqlDateFormat);
        }
        if (value instanceof LocalDate ldt) {
            return " < TO_DATE('%s', '%s')".formatted(sdf.format(ldt), sqlDateFormat);
        }
        if (value instanceof Timestamp ts) {
            return " < TO_TIMESTAMP('%s', '%s')".formatted(sdf.format(ts), sqlDateFormat);
        }
        if (value instanceof String) {
            return " < '" + value + "'";
        }
        return " < " + value;

    }
}
