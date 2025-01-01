package com.ainouss.jdatatools.batch.metadata;


import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)}.
 */
@Data
public class SqlPrimaryKey {

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
     * Column name.
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * Sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 represents the second column within the primary key).
     */
    @Column(name = "KEY_SEQ")
    private short keySeq;

    /**
     * Primary key name (may be null).
     */
    @Column(name = "PK_NAME")
    private String pkName;
}