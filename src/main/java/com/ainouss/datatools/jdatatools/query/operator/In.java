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
 * The IN operator allows you to specify multiple values in a WHERE clause.
 * <p>
 * The IN operator is a shorthand for multiple OR conditions.
 */
public class In extends Expression {

    private final List<Object> args = new ArrayList<>();

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

    public String sql() {
        String values = args.stream()
                .map(Object::toString)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));
        return " in  (" + values + ")";
    }
}
