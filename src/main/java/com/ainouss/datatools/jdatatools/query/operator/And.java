package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;

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
public class And extends Expression {

    /**
     * Constructs a new {@code And} operator with the given expression.
     *
     * @param expression The expression to add to the AND condition.
     */
    public And(Expression expression) {
        this.and.add(expression);
    }

    /**
     * Returns an empty string since the rendering of the AND condition
     * is handled by the parent {@code Expression} class.
     *
     * @return An empty string.
     */
    @Override
    protected String sql() {
        return "";
    }
}