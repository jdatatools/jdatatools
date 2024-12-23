package com.ainouss.datatools.jdatatools.query.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * An expression is defined on a path, has a value and can have a logical combination of embedded expressions using
 * (and, or, not)
 * An expression can have an SQL string
 */
public abstract class Expression implements Fragment {

    protected List<Expression> and = new ArrayList<>();
    protected List<Expression> or = new ArrayList<>();
    protected List<Expression> not = new ArrayList<>();

    /**
     * Adds to its ands
     *
     * @param expressions other expressions
     * @return this
     */
    public Expression and(Expression... expressions) {
        and.addAll(Arrays.asList(expressions));
        return this;
    }


    /**
     * Adds to its ors
     *
     * @param expressions other expressions
     * @return this
     */
    public Expression or(Expression... expressions) {
        or.addAll(Arrays.asList(expressions));
        return this;
    }

    /**
     * SQL that would be appended while rendering
     *
     * @return sql fragment
     */

    public String toSql() {

        String ands = this.and.stream()
                .map(Expression::toSql)
                .reduce((a, b) -> a + " and " + b)
                .orElse("");

        String ors = this.or.stream()
                .map(Expression::toSql)
                .reduce((a, b) -> a + " or " + b)
                .orElse("");

        String nots = this.not.stream()
                .map(Expression::toSql)
                .reduce((a, b) -> a + " and " + b)
                .orElse("");

        var builder = new StringBuilder(sql());

        if (!this.not.isEmpty()) {
            builder.append(" NOT (")
                    .append(nots)
                    .append(")");
        }

        if (isNotBlank(ands)) {
            builder.append(builder.isEmpty() ? "" : " and ")
                    .append("(")
                    .append(ands)
                    .append(")");
        }
        if (isNotBlank(ors)) {
            builder.append(builder.isEmpty() ? "" : " or ")
                    .append("(")
                    .append(ors)
                    .append(")");
        }

        return builder.toString();
    }

    public String sql() {
        return "";
    }
}
