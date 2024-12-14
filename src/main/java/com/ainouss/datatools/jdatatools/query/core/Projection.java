package com.ainouss.datatools.jdatatools.query.core;

public interface Projection<T> {

    String output();

    String attribute();

    Root<T> head();
}
