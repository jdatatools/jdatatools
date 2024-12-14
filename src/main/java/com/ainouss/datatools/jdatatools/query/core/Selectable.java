package com.ainouss.datatools.jdatatools.query.core;

public interface Selectable<T> {

    String toSql();

    String column();

    Root<T> root();
}
