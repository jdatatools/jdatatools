package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Represents the less than or equal operator (<=) in a query.
 * <p>
 * This class is used to compare if a value is less than or equal to
 * another value. It can be used with numbers and expressions.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.le(root.get("age"), 30)
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE age <= 30
 * </pre>
 */
public class Le extends Expression {

    /**
     * Constructs a new {@code Le} operator with the given path and value.
     *
     * @param path  The path representing the attribute to compare.
     * @param value The value to compare against.
     */
    public Le(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Generates the SQL representation of the less than or equal operator.
     *
     * @return The SQL representation of the operator.
     */
    public String sql() {

        if (value instanceof String) {
            return " <= '" + value + "'";
        }
        return " <= " + value;

    }
}