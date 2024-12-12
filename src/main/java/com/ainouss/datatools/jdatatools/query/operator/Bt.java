package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

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
 *  query.where(cb.bt(root.get("age"), 20, 40));
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE age BETWEEN 20 AND 40
 * </pre>
 */
public class Bt extends Expression {
    private final Object value1;
    private final Object value2;

    /**
     * Constructs a new {@code Bt} operator with the given path and range values.
     *
     * @param path   The path representing the attribute to compare.
     * @param value1 The lower bound of the range.
     * @param value2 The upper bound of the range.
     */
    public Bt(Path<?> path, Object value1, Object value2) {
        this.path = path;
        this.value1 = value1;
        this.value2 = value2;
        this.value = new Object[]{value1, value2};
    }

    /**
     * Generates the SQL representation of the BETWEEN operator.
     *
     * @return The SQL representation of the BETWEEN operator.
     * @throws RuntimeException If the operator is used with null values.
     */
    public String sql() {
        if (value == null || value1 == null || value2 == null) {
            throw new RuntimeException("Between operator should be used with a two non null values");
        }
        return " between " + value1 + " and " + value2;
    }
}