package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Represents the equality operator (=) in a query.
 * <p>
 * This class is used to compare if a value is equal to another value.
 * It can be used with numbers, paths, and strings.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(cb.eq(root.get("name"), "John Doe"));
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE name = 'John Doe'
 * </pre>
 */
public class Eq implements Expression {

    private final Selectable attribute;
    private final Selectable right;

    /**
     * Constructs a new {@code Eq} operator with the given path and value.
     *
     * @param attribute The path representing the attribute to compare.
     * @param right     The value to compare against.
     */
    public Eq(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    /**
     * Generates the SQL representation of the equality operator.
     *
     * @return The SQL representation of the equality operator.
     * @throws RuntimeException If the operator is used with null or array values.
     */
    public String toSql() {
        return attribute.toSql() + " = " + right.toSql();
    }
}