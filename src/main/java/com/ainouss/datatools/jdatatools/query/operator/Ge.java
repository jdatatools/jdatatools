package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

/**
 * Represents the greater than or equal operator (>=) in a query.
 * <p>
 * This class is used to compare if a value is greater than or equal to
 * another value. It can be used with numbers, paths, and strings.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(cb.ge(root.get("salary"), 30));
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE salary >= 30
 * </pre>
 */
public class Ge extends Expression {

    private final Selectable attribute;
    private final Selectable right;

    /**
     * Constructs a new {@code Ge} operator with the given path and value.
     *
     * @param attribute The path representing the attribute to compare.
     * @param right     The value to compare against.
     */
    public Ge(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    /**
     * Generates the SQL representation of the greater than or equal operator.
     *
     * @return The SQL representation of the operator.
     * @throws RuntimeException If the operator is used with null values.
     */
    public String toString() {
        return attribute + " >= " + right;
    }
}