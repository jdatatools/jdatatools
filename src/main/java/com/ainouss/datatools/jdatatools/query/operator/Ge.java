package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

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
 *  query.where(cb.ge(root.get("age"), 30));
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE age >= 30
 * </pre>
 */
public class Ge extends Expression {

    /**
     * Constructs a new {@code Ge} operator with the given path and value.
     *
     * @param path  The path representing the attribute to compare.
     * @param value The value to compare against.
     */
    public Ge(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Generates the SQL representation of the greater than or equal operator.
     *
     * @return The SQL representation of the operator.
     * @throws RuntimeException If the operator is used with null values.
     */
    public String sql() {
        if (value == null) {
            throw new RuntimeException("Greater than operator should be used with a single expression or a non null value.");
        }
        if (value instanceof String) {
            return " >= '" + value + "'";
        }
        return " >= " + value;
    }
}