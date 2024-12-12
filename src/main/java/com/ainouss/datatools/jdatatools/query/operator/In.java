package com.ainouss.datatools.jdatatools.query.operator;

import com.ainouss.datatools.jdatatools.query.core.Expression;
import com.ainouss.datatools.jdatatools.query.core.Path;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the IN operator in a query.
 * <p>
 * This class is used to check if a value matches any value within a given list
 * or collection. It can be used with numbers, paths, and strings.
 * <p>
 * Example usage:
 * <pre>
 *  CriteriaBuilder cb = new CriteriaBuilder();
 *  CriteriaQuery<MyEntity> query = cb.createQuery(MyEntity.class);
 *  Root<MyEntity> root = query.from();
 *  query.where(
 *      cb.in(
 *          root.get("country"), Arrays.asList("USA", "Canada")
 *      )
 *  );
 * </pre>
 * This would generate the following SQL WHERE clause:
 * <pre>
 *  WHERE country IN ('USA', 'Canada')
 * </pre>
 */
public class In extends Expression {

    private final List<Object> args = new ArrayList<>();

    /**
     * Constructs a new {@code In} operator with the given path and values.
     *
     * @param path   The path representing the attribute to compare.
     * @param value  The value or collection of values to compare against.
     * @param values Additional values to be included in the comparison.
     * @throws RuntimeException If no values are provided or if the value is null.
     */
    public In(Path<?> path, Object value, Object[] values) {
        this.path = path;
        this.value = value;
        if (value == null) {
            throw new RuntimeException("In operator should be used with at least one non null value");
        }
        if (value instanceof Collection<?> val) {
            args.addAll(val);
        } else if (value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++) {
                args.add(Array.get(value, i));
            }
        } else {
            args.add(value);
        }
        if (values != null) {
            args.addAll(Arrays.asList(values));
        }

        if (args.isEmpty()) {
            throw new RuntimeException("In should be used with a non empty array");
        }
    }

    /**
     * Generates the SQL representation of the IN operator.
     *
     * @return The SQL representation of the IN operator.
     */
    public String sql() {
        String values = args.stream()
                .map(Object::toString)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));
        return " in  (" + values + ")";
    }
}