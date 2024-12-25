package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

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

    /**
     * Constructs a new {@code Like} operator with the given path and value.
     *
     * @param attribute The attribute representing the attribute to compare.
     * @param right     The value used for pattern matching.
     */

    public Like(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    /**
     * Generates the SQL representation of the LIKE operator.
     *
     * @return The SQL representation of the LIKE operator.
     */
    public String toSql() {
        String escaped = right.toSql().replace("'", "");
        return attribute.toSql() + " like '%" + escaped + "%'";
    }
}