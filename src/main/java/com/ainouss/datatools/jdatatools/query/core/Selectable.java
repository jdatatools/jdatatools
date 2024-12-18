package com.ainouss.datatools.jdatatools.query.core;

public interface Selectable {

    String column();

    Root<?> root();

    String toSql();

}
