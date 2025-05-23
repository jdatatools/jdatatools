package com.ainouss.jdatatools.query.logical;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * Represents the logical AND operator in a query.
 * <p>
 * This class is used to combine multiple expressions into a single
 * conditional statement where all expressions must evaluate to true
 * for the overall condition to be true.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.and(
 *          cb.eq(root.get("name"), "John Doe"),
 *          cb.gt(root.get("salary"), 30)
 *      )
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE name = 'John Doe' AND salary > 30
 * </pre>
 */
public class And extends AbstractExpression {

    /**
     * Constructs a new {@code And} operator with the given expression.
     *
     * @param expression The expression to add to the AND condition.
     * @param sqlDialect The SQL dialect to use for rendering.
     */
    public And(Expression expression, SqlDialect sqlDialect) {
        super(sqlDialect);
        this.and.add(expression);
    }
}