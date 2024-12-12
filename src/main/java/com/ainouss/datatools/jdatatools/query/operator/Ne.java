package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Expression;

import static org.springframework.util.ObjectUtils.isArray;

/**
 * Represents the inequality operator (!=) in a query.
 * <p>
 * This class is used to compare if a value is not equal to another value.
 * It can be used with numbers, paths, and strings.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.ne(root.get("name"), "John Doe")
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE name != 'John Doe'
 * </pre>
 */
public class Ne extends Expression {

    /**
     * Constructs a new {@code Ne} operator with the given path and value.
     *
     * @param path  The path representing the attribute to compare.
     * @param value The value to compare against.
     */
    public Ne(Path<?> path, Object value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Generates the SQL representation of the inequality operator.
     *
     * @return The SQL representation of the inequality operator.
     * @throws RuntimeException If the operator is used with null or array values.
     */
    public String sql() {
        if (value == null || isArray(value)) {
            throw new RuntimeException("Not equal should be used with a single non null value");
        }
        var sql = " != ";
        if (value instanceof Number || value instanceof Path<?>) {
            return sql + value;
        }
        return sql + "'" + value + "'";
    }
}