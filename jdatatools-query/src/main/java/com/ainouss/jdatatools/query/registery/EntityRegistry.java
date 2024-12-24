package com.ainouss.jdatatools.query.registery;

import com.ainouss.jdatatools.query.core.CriteriaBuilder;
import com.ainouss.jdatatools.query.core.Path;
import com.ainouss.jdatatools.query.core.Root;
import com.ainouss.jdatatools.query.core.Selectable;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.ainouss.jdatatools.query.util.DataUtils.isNotBlank;

/**
 * Creates a criteria builder and holds column names and table names
 */
@Component
@Slf4j
public class EntityRegistry {
    /**
     * columns from @Column annotation
     */
    public static final LinkedHashMap<Path<?>, String> paths = new LinkedHashMap<>();
    /**
     * tables from @Table annotation
     */
    public static final LinkedHashMap<Root<?>, String> roots = new LinkedHashMap<>();

    private static final HashSet<Class<?>> registered = new HashSet<>();


    public EntityRegistry(EntityResolver resolver) {
        resolver
                .resolve()
                .forEach(this::register);
    }

    /**
     * Entry criteria builder
     *
     * @return criteria builder
     */
    public static CriteriaBuilder criteriaBuilder() {
        return new CriteriaBuilder();
    }

    /**
     * Scan the class for table name & columns
     *
     * @param clazz input
     */
    public void register(Class<?> clazz) {
        registerClass(clazz);
    }

    /**
     * Register class
     *
     * @param clazz input
     */
    public static void registerClass(Class<?> clazz) {
        if (registered.contains(clazz)) {
            return;
        }
        Root<?> root = new Root<>(clazz);
        roots.put(root, getTableName(clazz));
        getSelectableFields(clazz)
                .forEach(field -> paths.put(new Path<>(root, field.getName()), field.getAnnotation(Column.class).name()));
        registered.add(clazz);
    }

    private static String getTableName(Class<?> model) {
        if (model == null) {
            throw new RuntimeException("Target class should not be null");
        }
        Table annotation = AnnotationUtils.findAnnotation(model, Table.class);
        if (annotation == null) {
            String message = String.format("Target class %s should be annotated with annotation %s", model, Table.class);
            throw new RuntimeException(message);
        }
        return annotation.name();
    }


    /**
     * Resolve path to a column name, all paths are known before the query execution
     *
     * @return column name
     */
    public static String fullResolve(Selectable selectable) {
        if (selectable == null) {
            return "";
        }
        if (selectable instanceof Root<?> root) {
            return resolveRoot(root);
        }
        if (selectable instanceof Path<?> path) {
            return fullResolvePath(path);
        }
        return selectable.toSql();
    }

    public static String fullResolvePath(Path<?> path) {
        String root = resolveRoot(path.getHead());
        String col = resolvePath(path);
        return root + "." + (isNotBlank(col) ? col : path.getAttribute());
    }

    public static String resolvePath(Path<?> path) {
        String col = paths.get(path);
        return (isNotBlank(col) ? col : path.getAttribute());
    }

    /**
     * Resolve root to a column name, all paths are known before the query execution
     *
     * @param root root
     * @return column name
     */
    public static String resolveRoot(Root<?> root) {
        if (root == null) {
            return "";
        }
        if (isNotBlank(root.getAlias())) {
            return root.getAlias();
        }
        String table = roots.get(root);
        if (table == null) {
            throw new RuntimeException("Could not locate table for root, make sure to add @Column annotation" + root);
        }
        return table;
    }

    /**
     * List of selectable fields for a given class
     *
     * @param clazz class
     * @return fields
     */
    public static List<Field> getSelectableFields(Class<?> clazz) {
        return Arrays.stream(
                        FieldUtils.getFieldsWithAnnotation(clazz, Column.class)
                )
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

}
