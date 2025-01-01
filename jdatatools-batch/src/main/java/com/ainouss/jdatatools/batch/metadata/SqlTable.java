package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}.
 */
@Data
public class SqlTable {

    /**
     * Table catalog (may be null).
     */
    @Column(name = "TABLE_CAT")
    private String tableCat;

    /**
     * Table schema (may be null).
     */
    @Column(name = "TABLE_SCHEM")
    private String tableSchem;

    /**
     * Table name.
     */
    @Column(name = "TABLE_NAME")
    private String tableName;

    /**
     * Table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    @Column(name = "TABLE_TYPE")
    private String tableType;

    /**
     * Explanatory comment on the table (may be null).
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * The types catalog (may be null).
     */
    @Column(name = "TYPE_CAT")
    private String typeCat;

    /**
     * The types schema (may be null).
     */
    @Column(name = "TYPE_SCHEM")
    private String typeSchem;

    /**
     * Type name (may be null).
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Name of the designated "identifier" column of a typed table (may be null).
     */
    @Column(name = "SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    /**
     * Specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
     */
    @Column(name = "REF_GENERATION")
    private String refGeneration;
}