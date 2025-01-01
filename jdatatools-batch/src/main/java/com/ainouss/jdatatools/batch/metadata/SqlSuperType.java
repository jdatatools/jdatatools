package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getSuperTypes(String, String, String)}.
 */
@Data
public class SqlSuperType {

    /**
     * The catalog of the type.
     */
    @Column(name = "TYPE_CAT")
    private String typeCat;

    /**
     * The schema of the type.
     */
    @Column(name = "TYPE_SCHEM")
    private String typeSchem;

    /**
     * The name of the type.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * The catalog of the direct supertype.
     */
    @Column(name = "SUPERTYPE_CAT")
    private String supertypeCat;

    /**
     * The schema of the direct supertype.
     */
    @Column(name = "SUPERTYPE_SCHEM")
    private String supertypeSchem;

    /**
     * The name of the direct supertype.
     */
    @Column(name = "SUPERTYPE_NAME")
    private String supertypeName;
}