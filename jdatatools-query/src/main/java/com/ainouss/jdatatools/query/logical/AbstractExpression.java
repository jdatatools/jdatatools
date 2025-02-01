package com.ainouss.jdatatools.query.logical;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An expression is defined on a path, has a value and can have a logical combination of embedded expressions using
 * (and, or, not)
 * An expression can have an SQL string
 */
public abstract class AbstractExpression implements Expression {

    protected List<Expression> and = new ArrayList<>();
    protected List<Expression> or = new ArrayList<>();
    private final SqlDialect sqlDialect; // Dialect Integration

    public AbstractExpression(SqlDialect sqlDialect) { // Dialect Integration
        this.sqlDialect = sqlDialect;
    }
    public AbstractExpression() { // For CTE - default constructor // Dialect Integration
        this(null);
    }


    /**
     * Adds to its ands
     *
     * @param expressions other expressions
     * @return this
     */
    public AbstractExpression and(Expression... expressions) {
        and.addAll(Arrays.asList(expressions));
        return this;
    }


    /**
     * Adds to its ors
     *
     * @param expressions other expressions
     * @return this
     */
    public AbstractExpression or(Expression... expressions) {
        or.addAll(Arrays.asList(expressions));
        return this;
    }

    /**
     * SQL that would be appended while rendering
     *
     * @return sql fragment
     */

    public String toSql() {

        StringBuilder sqlBuilder = new StringBuilder();
        boolean hasContent = false;

        if (!and.isEmpty()) {
            sqlBuilder.append("(");
            boolean first = true;
            for (Expression expression : and) {
                if (!first) {
                    sqlBuilder.append(" and ");
                }
                sqlBuilder.append(expression.toSql());
                first = false;
            }
            sqlBuilder.append(")");
            hasContent = true;
        }

        if (!or.isEmpty()) {
            if (hasContent) {
                sqlBuilder.append(" or ");
            }
            sqlBuilder.append("(");
            boolean first = true;
            for (Expression expression : or) {
                if (!first) {
                    sqlBuilder.append(" or ");
                }
                sqlBuilder.append(expression.toSql());
                first = false;
            }
            sqlBuilder.append(")");
        }
        return sqlBuilder.toString();
    }
}