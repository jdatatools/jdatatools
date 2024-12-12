package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * An expression is defined on a path, has a value and can have a logical combination of embedded expressions using
 * (and, or, not)
 * An expression can have an SQL string
 */
public abstract class Expression {

    protected Expression expression;
    protected Object value;
    protected Path<?> path;
    protected List<Expression> and = new ArrayList<>();
    protected List<Expression> or = new ArrayList<>();
    protected List<Expression> not = new ArrayList<>();

    /**
     * Adds to its ands
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return this
     */
    public Expression and(Expression expression, Expression... expressions) {
        and.add(expression);
        and.addAll(Arrays.asList(expressions));
        return this;
    }


    /**
     * Adds to its ors
     *
     * @param expression  expression
     * @param expressions other expressions
     * @return this
     */
    public Expression or(Expression expression, Expression... expressions) {
        or.add(expression);
        or.addAll(Arrays.asList(expressions));
        return this;
    }

    /**
     * SQL that would be appended while rendering
     *
     * @return sql fragment
     */
    protected abstract String sql();

    protected String render() {

        String ands = this.and
                .stream()
                .map(Expression::render)
                .collect(Collectors.joining(" and "));

        String ors = this.or
                .stream()
                .map(Expression::render)
                .collect(Collectors.joining(" or "));

        String nots = this.not
                .stream()
                .map(Expression::render)
                .collect(Collectors.joining(" and "));

        var sql = new StringBuilder(EntityRegistry.fullResolve(this.path));

        sql.append(this.sql());

        if (expression != null) {
            sql.append("(")
                    .append(expression.render())
                    .append(")");
        }

        if (!this.not.isEmpty()) {
            sql.append("(")
                    .append(nots)
                    .append(")");
        }

        if (isNotBlank(ands)) {
            sql.append(sql.isEmpty() ? "" : " and ")
                    .append("(")
                    .append(ands)
                    .append(")");
        }
        if (isNotBlank(ors)) {
            sql.append(sql.isEmpty() ? "" : " or ")
                    .append("(")
                    .append(ors)
                    .append(")");
        }
        return sql.toString();
    }
}
