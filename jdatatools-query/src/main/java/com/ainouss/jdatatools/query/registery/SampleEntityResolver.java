package com.ainouss.jdatatools.query.registery;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleEntityResolver implements EntityResolver {

    private final List<Class<?>> entities;

    public SampleEntityResolver(List<Class<?>> entities) {
        this.entities = entities;
    }

    @Override
    public List<Class<?>> resolve() {
        return entities;
    }
}
