package com.ainouss.jdatatools.query.operator;

import com.ainouss.jdatatools.query.core.Expression;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the IN operator in a query.
 * <p>
 * This class is used to check if a value matches any value within a given list
 * or collection. It can be used with numbers, paths, and strings.
 * <p>
 * Example usage 1:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.inL(
 *          root.get("country"), Arrays.asList("USA", "Canada")
 *      )
 *  );
 * </pre>
 * would generate the following WHERE clause :
 * <pre> WHERE country IN ('USA', 'Canada') </pre>
 * <p>
 * Example usage 2:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.in(
 *          root.get("country"), "USA", "Canada"
 *      )
 *  );
 * </pre>
 * <p>
 * would generate the following WHERE clause (the same as above):
 * <pre> WHERE country IN ('USA', 'Canada') </pre>
 * Example usage 3:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.inn(
 *          root.get("country"), List.of()
 *      )
 *  );
 * </pre>
 * This would generate an empty SQL WHERE clause.
 */
public class In implements Expression {

    private final Selectable attribute;
    private final List<Selectable> args = new ArrayList<>();
    private final SqlDialect sqlDialect;

    /**
     * Constructs a new {@code In} operator with the given path and values.
     *
     * @param attribute The attribute representing the attribute to compare.
     * @param args      The collection of values to compare against.
     * @param sqlDialect The SQL dialect to use for rendering.
     * @throws RuntimeException If no values are provided or if the value is null.
     */
    public In(Selectable attribute, Collection<Selectable> args, SqlDialect sqlDialect) {
        this.attribute = attribute;
        this.args.addAll(args);
        this.sqlDialect = sqlDialect;
    }

    /**
     * Generates the SQL representation of the 'IN' operator.
     *
     * @return The SQL representation of the 'IN' operator.
     */
    public String toSql() {
        String values = args.stream()
                .map(Selectable::toSql)
                .collect(Collectors.joining(","));
        return sqlDialect.escapeIdentifier(attribute.toSql()) + " in  (" + values + ")";
    }
}