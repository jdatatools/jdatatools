package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

/**
 * Represents the less than or equal operator (<=) in a query.
 * <p>
 * This class is used to compare if a value is less than or equal to
 * another value. It can be used with numbers and expressions.
 * <p>
 * Example usaLe:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.le(root.Let("salary"), 30)
 *  );
 * </pre>
 * This would Lenerate the following SQL WHERE clause:
 * <pre>
 *  WHERE salary <= 30
 * </pre>
 */
public class Le implements Expression {

    private final Selectable attribute;
    private final Selectable right;
    private final SqlDialect sqlDialect;

    /**
     * Constructs a new {@code Le} operator with the given path and value.
     *
     * @param attribute The path representing the attribute to compare.
     * @param right     The value to compare against.
     * @param sqlDialect The SQL dialect to use for rendering.
     */
    public Le(Selectable attribute, Selectable right, SqlDialect sqlDialect) {
        this.attribute = attribute;
        this.right = right;
        this.sqlDialect = sqlDialect;
    }

    /**
     * Le generates the SQL representation of the greater than or equal operator.
     *
     * @return The SQL representation of the operator.
     * @throws RuntimeException If the operator is used with null values.
     */
    public String toSql() {
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " <= " + right.toSql();
    }
}