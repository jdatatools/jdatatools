package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Alias;
import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.core.SqlDialect;
import com.ainouss.jdatatools.query.order.Order;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public abstract class AggregateFunction extends Alias implements Aggregable {

    protected final Selectable selectable;
    private final LinkedHashSet<Selectable> partitionBy = new LinkedHashSet<>();
    protected final LinkedHashSet<Order> orderBy = new LinkedHashSet<>();
    private final SqlDialect sqlDialect;

    protected AggregateFunction(SqlDialect sqlDialect) {
        this.selectable = null;
        this.sqlDialect = sqlDialect;
    }

    public AggregateFunction(Selectable selectable, SqlDialect sqlDialect) {
        this.selectable = selectable;
        this.sqlDialect = sqlDialect;
    }

    public abstract String sql();

    @Override
    public AggregateFunction partitionBy(Selectable... partitions) {
        partitionBy.addAll(Arrays.asList(partitions));
        return this;
    }

    @Override
    public AggregateFunction orderBy(Order... orders) {
        orderBy.addAll(Arrays.asList(orders));
        return this;
    }

    @Override
    public Aggregable over() {
        return this;
    }

    private String overSql() {
        if (partitionBy.isEmpty() && orderBy.isEmpty()) {
            return "";
        }
        return new StringBuilder(" over (")
                .append(partitions())
                .append(orders())
                .append(")")
                .toString();
    }

    private String orders() {
        if(orderBy.isEmpty()){
            return "";
        }
        String parts = orderBy.stream().map(Order::toSql).collect(Collectors.joining(","));
        return new StringBuilder(" order by ")
                .append(parts)
                .toString();
    }

    private String partitions() {
        if (partitionBy.isEmpty()) {
            return "";
        }
        return new StringBuilder("partition by ")
                .append(partitionBy.stream().map(Selectable::toSql).collect(Collectors.joining(",")))
                .toString();
    }

    @Override
    public String toSql() {
        return sql() + overSql();
    }



}