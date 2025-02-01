package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

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
    private final SqlDialect sqlDialect; // Dialect Integration

    /**
     * Constructs a new {@code Eq} operator with the given path and value.
     *
     * @param attribute The path representing the attribute to compare.
     * @param right     The value to compare against.
     * @param sqlDialect The SQL dialect to use for rendering. // Dialect Integration
     */
    public Eq(Selectable attribute, Selectable right, SqlDialect sqlDialect) { // Dialect Integration
        this.attribute = attribute;
        this.right = right;
        this.sqlDialect = sqlDialect;
    }

    /**
     * Generates the SQL representation of the equality operator.
     *
     * @return The SQL representation of the equality operator.
     * @throws RuntimeException If the operator is used with null or array values.
     */
    public String toSql() {
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " = " + right.toSql(); // Dialect Integration
    }
}