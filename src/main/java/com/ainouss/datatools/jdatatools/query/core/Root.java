package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.registery.EntityRegistry;
import lombok.Getter;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Root object, equivalent to a table in RDBMS
 * Alias is a SQL alias for table name
 * Every root should be annotated with {@link jakarta.persistence.Table} annotation
 *
 * @param <T> Java class
 */
@Getter
public class Root<T> extends Alias implements Selectable, Source, Joinable<T> {

    private final Class<T> javaType;

    private final String table;

    private String schema;

    public Root(Class<T> from) {
        this.javaType = from;
        this.alias = EntityRegistry.roots.get(this);
        this.table = EntityRegistry.roots.get(this);
    }

    public Root(String name) {
        this.javaType = null;
        this.alias = name;
        this.table = name;
    }

    /**
     * A path that starts from this root
     *
     * @param attribute java field name
     * @return path
     */
    public Path<T> get(String attribute) {
        return new Path<>(this, attribute);
    }


    /**
     * SQL table alias
     *
     * @param alias alias
     * @return alias
     */
    public Root<T> as(String alias) {
        this.alias = alias;
        return this;
    }

    public Root<T> schema(String schema) {
        this.schema = schema;
        return this;
    }

    public String schema() {
        if (isBlank(schema)) {
            return "";
        }
        return schema + ".";
    }

    @Override
    public String toSql() {
        return alias;
    }

    @Override
    public String getName() {
        return new StringBuilder(schema())
                .append(EntityRegistry.roots.get(this))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root<?> root = (Root<?>) o;
        return Objects.equals(javaType, root.javaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaType);
    }

    @Override
    public Source getSelf() {
        return this;
    }

}
