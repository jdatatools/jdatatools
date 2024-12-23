package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Selectable;

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

    private final Selectable left;
    private final Selectable right;

    public Ne(Selectable left, Selectable right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Generates the SQL representation of the inequality operator.
     *
     * @return The SQL representation of the inequality operator.
     * @throws RuntimeException If the operator is used with null or array values.
     */
    public String sql() {
        return left.toSql() + " != " + right.toSql();
    }
}