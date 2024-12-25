package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;

/**
 * Represents the greater than operator (>) in a query.
 * <p>
 * This class is used to compare if a value is greater than another value.
 * It can be used with numbers, paths, and strings.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.gt(root.get("salary"), 30)
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE salary > 30
 * </pre>
 */
public class Gt implements Expression {

    private final Selectable attribute;
    private final Selectable right;

    /**
     * Constructs a new {@code Gt} operator with the given path and value.
     *
     * @param attribute The attribute representing the attribute to compare.
     * @param right     The value to compare against.
     */
    public Gt(Selectable attribute, Selectable right) {
        this.attribute = attribute;
        this.right = right;
    }

    /**
     * Generates the SQL representation of the greater than operator.
     */
    public String toSql() {
        return attribute.toSql() + " > " + right.toSql();
    }
}