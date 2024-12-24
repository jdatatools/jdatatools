package com.ainouss.jdatatools.query.registery;

import java.util.List;

/**
 * Define an abstract way to load entities
 */
@FunctionalInterface
public interface EntityResolver {
    /**
     * Defines a supplier to get the model
     *
     * @return list of classes
     */
    List<Class<?>> resolve();

}
