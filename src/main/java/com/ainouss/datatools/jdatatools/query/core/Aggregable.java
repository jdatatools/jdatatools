package com.ainouss.datatools.jdatatools.query.core;

public interface Aggregable extends Selectable {

    AggregateFunction partitionBy(Selectable... partitions);

    AggregateFunction orderBy(Order... orders);

    Aggregable over();

}
