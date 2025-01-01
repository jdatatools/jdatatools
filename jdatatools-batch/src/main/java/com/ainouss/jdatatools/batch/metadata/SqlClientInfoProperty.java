package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getClientInfoProperties()}.
 */
@Data
public class SqlClientInfoProperty {

    /**
     * The name of the client info property.
     */
    @Column(name = "NAME")
    private String name;

    /**
     * A description of the property.
     */
    @Column(name = "MAX_LEN")
    private int maxLen;

    /**
     * The default value of the property.
     */
    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;

    /**
     * The description of the property.
     */
    @Column(name = "DESCRIPTION")
    private String description;
}