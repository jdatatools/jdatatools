package com.ainouss.jdatatools.query.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * An expression is defined on a path, has a value and can have a logical combination of embedded expressions using
 * (and, or, not)
 * An expression can have an SQL string
 */
public abstract class AbstractExpression implements Fragment {

    protected List<AbstractExpression> and = new ArrayList<>();
    protected List<AbstractExpression> or = new ArrayList<>();
    protected List<AbstractExpression> not = new ArrayList<>();

    /**
     * Adds to its ands
     *
     * @param expressions other expressions
     * @return this
     */
    public AbstractExpression and(AbstractExpression... expressions) {
        and.addAll(Arrays.asList(expressions));
        return this;
    }


    /**
     * Adds to its ors
     *
     * @param expressions other expressions
     * @return this
     */
    public AbstractExpression or(AbstractExpression... expressions) {
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
                .map(AbstractExpression::toSql)
                .reduce((a, b) -> a + " and " + b)
                .orElse("");

        String ors = this.or.stream()
                .map(AbstractExpression::toSql)
                .reduce((a, b) -> a + " or " + b)
                .orElse("");

        String nots = this.not.stream()
                .map(AbstractExpression::toSql)
                .reduce((a, b) -> a + " and " + b)
                .orElse("");

        var builder = new StringBuilder(sql());

        if (!this.not.isEmpty()) {
            builder.append(" not (")
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
