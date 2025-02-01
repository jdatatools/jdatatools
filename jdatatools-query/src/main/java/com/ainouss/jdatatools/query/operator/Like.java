package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.dialect.SqlDialect; // Dialect Integration

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
public class Like implements Expression {

    private final Selectable attribute;
    private final Selectable right;
    private final SqlDialect sqlDialect; // Dialect Integration

    /**
     * Constructs a new {@code Like} operator with the given path and value.
     *
     * @param attribute The attribute representing the attribute to compare.
     * @param right     The value used for pattern matching.
     * @param sqlDialect The SQL dialect to use for rendering. // Dialect Integration
     */
    public Like(Selectable attribute, Selectable right, SqlDialect sqlDialect) { // Dialect Integration
        this.attribute = attribute;
        this.right = right;
        this.sqlDialect = sqlDialect;
    }

    /**
     * Generates the SQL representation of the LIKE operator.
     *
     * @return The SQL representation of the LIKE operator.
     */
    public String toSql() {
        String escaped = right.toSql().replace("'", "");
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " like '%" + escaped + "%'"; // Dialect Integration
    }
}