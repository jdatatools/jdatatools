package com.ainouss.jdatatools.query.function;

import com.ainouss.jdatatools.query.core.Selectable;
import com.ainouss.jdatatools.query.order.Order;

public interface Aggregable extends Selectable {

    AggregateFunction partitionBy(Selectable... partitions);

    AggregateFunction orderBy(Order... orders);

    Aggregable over();

}
