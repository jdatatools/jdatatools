package com.ainouss.datatools.jdatatools.query.registery;

import com.ainouss.datatools.jdatatools.query.core.CriteriaBuilder;
import com.ainouss.datatools.jdatatools.query.core.Path;
import com.ainouss.datatools.jdatatools.query.core.Projection;
import com.ainouss.datatools.jdatatools.query.core.Root;
import com.ainouss.datatools.jdatatools.util.QueryBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Creates a criteria builder and holds column names and table names
 */
@Component
@Slf4j
public class EntityRegistry {
    /**
     * columns from @Column annotation
     */
    public static final LinkedHashMap<Path<?>, String> columns = new LinkedHashMap<>();
    /**
     * tables from @Table annotation
     */
    public static final LinkedHashMap<Root<?>, String> tables = new LinkedHashMap<>();

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
    public CriteriaBuilder getCriteriaBuilder() {
        return new CriteriaBuilder();
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
        Root<?> root = new Root<>(clazz);
        tables.put(root, getTableName(clazz));
        QueryBuilder.getSelectableFields(clazz)
                .forEach(field -> columns.put(new Path<>(root, field.getName()), field.getAnnotation(Column.class).name()));
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
    public static String resolve(Projection<?> projection) {
        if (projection == null) {
            return "";
        }
        String column = columns.get(new Path<>(projection.head(), projection.attribute()));
        if (column == null) {
            log.warn("Could not locate column for path {}, make sure to add a @Column annotation, the given attribute name will be used", projection);
            return projection.attribute();
        }
        return column;
    }

    /**
     * Resolve path to a column name, all paths are known before the query execution
     *
     * @param path path
     * @return column name
     */
    public static String fullResolve(Path<?> path) {
        if (path == null) {
            return "";
        }
        return resolve(path.getHead()) + "." + resolve(path);
    }

    /**
     * Resolve path to a column name, all paths are known before the query execution
     *
     * @param path path
     * @return column name
     */
    public static String resolve(Root<?> path) {
        if (path == null) {
            return "";
        }
        if (isNotBlank(path.getAlias())) {
            return path.getAlias();
        }
        String table = tables.get(path);
        if (table == null) {
            throw new RuntimeException("Could not locate table for path, make sure to add @Column annotation" + path);
        }
        return table;
    }

}
