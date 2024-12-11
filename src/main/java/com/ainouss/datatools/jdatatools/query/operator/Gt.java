package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.Expression;
import com.ainouss.datatools.jdatatools.query.Path;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Greater Than operator, denoted as ">", is used to compare values in a database table
 * and retrieve rows where a specific column's value is greater than a given criteria.
 */
public class Gt extends Expression {

    private String sqlDateFormat = "YYYYMMDD HH:MI:SS";
    private String df = "yyyyMMdd hh:mm:ss";
    final SimpleDateFormat sdf = new SimpleDateFormat(df);

    public Gt(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    public <R> Gt(Path<R> date, LocalDateTime localDateTime, String dateFormat) {
        this.path = date;
        this.value = localDateTime;
        this.sqlDateFormat = dateFormat;
    }

    public String sql() {
        if (value == null) {
            throw new RuntimeException("Greater than operator should be used with a single expression or a non null value.");
        }
        if (value instanceof LocalDateTime ldt) {
            return " > TO_DATE('%s', '%s')".formatted(sdf.format(ldt), sqlDateFormat);
        }
        if (value instanceof LocalDate ldt) {
            return " > TO_DATE('%s', '%s')".formatted(sdf.format(ldt), sqlDateFormat);
        }
        if (value instanceof Timestamp ts) {
            return " > TO_TIMESTAMP('%s', '%s')".formatted(sdf.format(ts), sqlDateFormat);
        }
        if (value instanceof String) {
            return " > '" + value + "'";
        }
        return " > " + value;
    }
}
