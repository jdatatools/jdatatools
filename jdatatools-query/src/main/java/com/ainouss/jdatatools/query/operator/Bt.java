package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Represents the BETWEEN operator in a query.
 * <p>
 * This class is used to check if a value is within a specified range.
 * It can be used with numbers and paths.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(cb.bt(root.get("salary"), 20, 40));
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE salary BETWEEN 20 AND 40
 * </pre>
 */
public class Bt extends Expression {

    private final Selectable attribute;
    private final Selectable left;
    private final Selectable right;

    /**
     * Constructs a new {@code Bt} operator with the given path and range values.
     *
     * @param selectable The path representing the attribute to compare.
     * @param left     The lower bound of the range.
     * @param right     The upper bound of the range.
     */
    public Bt(Selectable selectable, Selectable left, Selectable right) {
        this.attribute = selectable;
        this.left = left;
        this.right = right;
    }


    public String sql() {
        return attribute.toSql() + " between " + left.toSql() + " and " + right.toSql();
    }
}