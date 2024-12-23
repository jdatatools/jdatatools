package com.ainouss.datatools.jdatatools.query.core;

import com.ainouss.datatools.jdatatools.query.order.OrderDirection;
import lombok.Getter;

@Getter
public class Order implements Fragment {

    private final Selectable column;
    private final OrderDirection direction;

    public Order(Selectable column, OrderDirection direction) {
        this.column = column;
        this.direction = direction;
    }

    public Order(Selectable column) {
        this.column = column;
        this.direction = OrderDirection.ASC;
    }

    @Override
    public String toSql() {
        return column.toSql() + " " + direction;
    }
}
