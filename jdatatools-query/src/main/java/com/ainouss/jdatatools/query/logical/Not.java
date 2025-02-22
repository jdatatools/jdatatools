package com.ainouss.jdatatools.query.logical;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.dialect.SqlDialect;

/**
 * Represents the logical NOT operator in a query.
 * <p>
 * This class is used to negate the result of an expression.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.not(
 *          cb.eq(root.get("active"), true)
 *      )
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE NOT active = true
 * </pre>
 */
public class Not implements Expression {

    private final Expression expression;
    private final SqlDialect sqlDialect;

    /**
     * Constructs a new {@code Not} operator with the given expression.
     *
     * @param expression The expression to be negated.
     * @param sqlDialect The SQL dialect to use for rendering.
     */
    public Not(Expression expression, SqlDialect sqlDialect) {
        this.expression = expression;
        this.sqlDialect = sqlDialect;
    }

    @Override
    public String toSql() {
        String sql = expression.toSql();
        if (sql == null) {
            return "not null";
        }
        return "not (" + sql + ")";
    }
}