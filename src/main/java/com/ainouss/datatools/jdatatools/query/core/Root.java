package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.expression.IdentityExpression;
import com.ainouss.datatools.jdatatools.query.join.Join;
import com.ainouss.datatools.jdatatools.query.join.JoinExpression;
import com.ainouss.datatools.jdatatools.query.join.JoinType;
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
public class Root<T> extends Alias implements Selectable, Source {

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
     * Left join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    public <U> JoinExpression<T, U> leftJoin(Source root) {
        Join<T, U> join = new Join<>(this, root, JoinType.LEFT);
        return new JoinExpression<>(join);
    }

    /**
     * Right join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    public <U> JoinExpression<T, U> rightJoin(Source root) {
        Join<T, U> join = new Join<>(this, root, JoinType.RIGHT);
        return new JoinExpression<>(join);
    }

    /**
     * Inner join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    public <U> JoinExpression<T, U> innerJoin(Source root) {
        Join<T, U> join = new Join<>(this, root, JoinType.INNER);
        return new JoinExpression<>(join);
    }

    /**
     * Full join expression
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */

    public <U> JoinExpression<T, U> fullJoin(Source root) {
        Join<T, U> join = new Join<>(this, root, JoinType.FULL);
        return new JoinExpression<>(join);
    }

    /**
     * Cross join expression, does not have on clause, thus returns a join directly
     *
     * @param root target
     * @param <U>  target classs
     * @return join expression
     */
    public <U> Join<T, U> join(Source root) {
        Join<T, U> join = new Join<>(this, root, JoinType.CROSS);
        return new JoinExpression<>(join).on(new IdentityExpression());
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
}
