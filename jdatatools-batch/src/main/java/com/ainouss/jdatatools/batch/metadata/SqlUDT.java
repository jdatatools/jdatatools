package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getUDTs(String, String, String, int[])}.
 */
@Data
public class SqlUDT {

    /**
     * The catalog of the UDT.
     */
    @Column(name = "TYPE_CAT")
    private String typeCat;

    /**
     * The schema of the UDT.
     */
    @Column(name = "TYPE_SCHEM")
    private String typeSchem;

    /**
     * The name of the UDT.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Class that implements the UDT.
     */
    @Column(name = "CLASS_NAME")
    private String className;

    /**
     * The type of the UDT.
     * <ul>
     * <li>java.sql.Types.STRUCT
     * <li>java.sql.Types.DISTINCT
     * <li>java.sql.Types.JAVA_OBJECT
     * </ul>
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Remarks about the UDT.
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * Indicates how the columns of the UDT are instantiatable.
     */
    @Column(name = "BASE_TYPE")
    private Short baseType;
}