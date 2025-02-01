package com.ainouss.jdatatools.query.logical;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

/**
 * Represents the logical OR operator in a query.
 * <p>
 * This class is used to combine multiple expressions into a single
 * conditional statement where at least one expression must evaluate
 * to true for the overall condition to be true.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.or(
 *          cb.eq(root.get("city"), "New York"),
 *          cb.eq(root.get("city"), "Los Angeles")
 *      )
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE city = 'New York' OR city = 'Los Angeles'
 * </pre>
 */
public class Or extends AbstractExpression {

    /**
     * Constructs a new {@code Or} operator with the given expression.
     *
     * @param expression The expression to add to the OR condition.
     * @param sqlDialect The SQL dialect to use for rendering. // Dialect Integration
     */
    public Or(Expression expression, SqlDialect sqlDialect) { // Dialect Integration
        super(sqlDialect); // Dialect Integration
        this.or.add(expression);
    }

}