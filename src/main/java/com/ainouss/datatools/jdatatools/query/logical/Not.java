package com.ainouss.datatools.jdatatools.query.logical;

import com.ainouss.datatools.jdatatools.query.core.Expression;

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
public class Not extends Expression {

    /**
     * Constructs a new {@code Not} operator with the given expression.
     *
     * @param expression The expression to be negated.
     */
    public Not(Expression expression) {
        this.not.add(expression);
    }
}