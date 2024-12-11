package com.ainouss.datatools.jdatatools.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An expression is defined on a path, has a value and can have a logical combination of embedded expressions using
 * (and, or, not)
 * An expression can have a sql string
 */
public abstract class Expression {

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

    protected boolean hasAny() {
        return and.isEmpty() || or.isEmpty() || not.isEmpty();
    }

}
