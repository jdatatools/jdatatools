package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

/**
 * Represents the LIKE operator in a query.
 * <p>
 * This class is used for pattern matching against string values.
 * It adds wildcard characters (%) around the provided value to match
 * any substring within the target attribute.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.like(root.get("name"), "John")
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE name LIKE '%John%'
 * </pre>
 */
public class Like extends Expression {

    /**
     * Constructs a new {@code Like} operator with the given path and value.
     *
     * @param path  The path representing the attribute to compare.
     * @param value The value used for pattern matching.
     */
    public Like(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Generates the SQL representation of the LIKE operator.
     *
     * @return The SQL representation of the LIKE operator.
     */
    public String sql() {
        return " like '%" + value + "%'";
    }
}