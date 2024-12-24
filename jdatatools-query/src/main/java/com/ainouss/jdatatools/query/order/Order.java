package com.ainouss.jdatatools.query.order;

import com.ainouss.jdatatools.query.core.Fragment;
import com.ainouss.jdatatools.query.core.Selectable;
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
