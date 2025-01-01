package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.DatabaseMetaData;

/**
 * Represents the data returned by
 * {@link DatabaseMetaData#getVersionColumns(String, String, String)}.
 */
@Data
public class SqlVersionColumn {

    /**
     * Column name.
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * SQL data type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Data source dependent type name.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Column size.
     */
    @Column(name = "COLUMN_SIZE")
    private int columnSize;

    /**
     * The number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
     */
    @Column(name = "DECIMAL_DIGITS")
    private Integer decimalDigits;

    /**
     * Pseudo column usage.
     * <ul>
     * <li>versionColumnUnknown - may or may not be a pseudo column
     * <li>versionColumnPseudo - is a pseudo column
     * <li>versionColumnNotPseudo - is NOT a pseudo column
     * </ul>
     */
    @Column(name = "PSEUDO_COLUMN")
    private short pseudoColumn;
}