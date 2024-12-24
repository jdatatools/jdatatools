package com.ainouss.jdatatools.query.core;

import com.ainouss.jdatatools.query.order.OrderDirection;
import com.ainouss.jdatatools.query.registery.EntityRegistry;
import lombok.Getter;

import java.util.Objects;

/**
 * Description of a path to an attribute or join or selection
 *
 * @param <T> Type
 */
@Getter
public class Path<T> extends Alias implements Selectable {

    protected final Root<T> head;

    protected final String attribute;


    public Path(Root<T> head, String attribute) {
        this.head = head;
        this.attribute = attribute;
        this.alias = attribute;
    }

    public Path(Class<T> head, String attribute) {
        this.attribute = attribute;
        this.head = new Root<>(head);
        this.alias = attribute;
    }

    public Path(String name, String attr) {
        this.head = new Root<>(name);
        this.attribute = attr;
        this.alias = attr;
    }

    public final Path<?> get(String id) {
        return new Path<>(this.head, id);
    }

    public final Order asc() {
        return new Order(this, OrderDirection.ASC);
    }
    public final Order desc() {
        return new Order(this, OrderDirection.ASC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path<?> path = (Path<?>) o;
        return Objects.equals(head, path.head) && Objects.equals(attribute, path.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, attribute);
    }


    @Override
    public String toSql() {
        return EntityRegistry.fullResolve(this);
    }
}
