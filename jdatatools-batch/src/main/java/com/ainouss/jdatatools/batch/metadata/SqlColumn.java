package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}.
 */
@Data
public class SqlColumn {

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
     * SQL type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Data source dependent type name, for a UDT it is its fully qualified SQL type name.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Column size.
     */
    @Column(name = "COLUMN_SIZE")
    private int columnSize;

    /**
     * Unused.
     */
    @Column(name = "BUFFER_LENGTH")
    private Integer bufferLength;

    /**
     * The number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
     */
    @Column(name = "DECIMAL_DIGITS")
    private Integer decimalDigits;

    /**
     * Radix (typically either 10 or 2).
     */
    @Column(name = "NUM_PREC_RADIX")
    private Integer numPrecRadix;

    /**
     * Is NULL allowed.
     * <ul>
     * <li>columnNoNulls - might not allow NULL values
     * <li>columnNullable - definitely allows NULL values
     * <li>columnNullableUnknown - nullability unknown
     * </ul>
     */
    @Column(name = "NULLABLE")
    private int nullable;

    /**
     * Comment describing column (may be null).
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * Default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null).
     * (May be null if default is not specified)
     */
    @Column(name = "COLUMN_DEF")
    private String columnDef;

    /**
     * SQL data type as defined in ISO 9075.
     */
    @Column(name = "SQL_DATA_TYPE")
    private int sqlDataType;

    /**
     * SQL datetime subcode as defined in ISO 9075.
     */
    @Column(name = "SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    /**
     * For char types the maximum number of bytes in the column.
     */
    @Column(name = "CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    /**
     * The ordinal position of the column in the table (starting from 1).
     */
    @Column(name = "ORDINAL_POSITION")
    private int ordinalPosition;

    /**
     * Is column nullable.
     * <ul>
     * <li> YES           --- if the column can include NULLs
     * <li> NO            --- if the column cannot include NULLs
     * <li> empty string  --- if the nullability for the column is unknown
     * </ul>
     */
    @Column(name = "IS_NULLABLE")
    private String isNullable;

    /**
     * Indicates whether this column is auto incremented.
     * <ul>
     * <li> YES           --- if the column is auto incremented
     * <li> NO            --- if the column is not auto incremented
     * <li> empty string  --- if it cannot be determined whether the column is auto incremented
     * </ul>
     */
    @Column(name = "IS_AUTOINCREMENT")
    private String isAutoincrement;

    /**
     * Indicates whether this is a generated column.
     * <ul>
     * <li> YES           --- if this is a generated column
     * <li> NO            --- if this is not a generated column
     * <li> empty string  --- if it cannot be determined whether this is a generated column
     * </ul>
     */
    @Column(name = "IS_GENERATEDCOLUMN")
    private String isGeneratedcolumn;
}