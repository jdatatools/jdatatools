package com.ainouss.jdatatools.batch.metadata;


import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
 */
@Data
public class SqlFunctionColumn {

    /**
     * Function catalog (may be null).
     */
    @Column(name = "FUNCTION_CAT")
    private String functionCat;

    /**
     * Function schema (may be null).
     */
    @Column(name = "FUNCTION_SCHEM")
    private String functionSchem;

    /**
     * Function name.
     */
    @Column(name = "FUNCTION_NAME")
    private String functionName;

    /**
     * Column name.
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * Kind of column/parameter.
     * <ul>
     *     <li>functionColumnUnknown - The kind of parameter is unknown
     *     <li>functionColumnIn - IN parameter
     *     <li>functionColumnInOut - INOUT parameter
     *     <li>functionColumnOut - OUT parameter
     *     <li>functionReturn - Function return value
     *     <li>functionColumnResult - Column in table return
     * </ul>
     */
    @Column(name = "COLUMN_TYPE")
    private short columnType;

    /**
     * SQL type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Data source dependent type name.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Precision.
     */
    @Column(name = "PRECISION")
    private int precision;

    /**
     * Length in bytes of data.
     */
    @Column(name = "LENGTH")
    private int length;

    /**
     * Scale - null is returned for data types where SCALE is not applicable.
     */
    @Column(name = "SCALE")
    private Short scale;

    /**
     * Radix.
     */
    @Column(name = "RADIX")
    private Short radix;

    /**
     * Indicates whether NULL is allowed.
     * <ul>
     *     <li>functionNoNulls - Does not allow NULL values
     *     <li>functionNullable - Allows NULL values
     *     <li>functionNullableUnknown - Nullability unknown
     * </ul>
     */
    @Column(name = "NULLABLE")
    private short nullable;

    /**
     * Remarks describing parameter/column.
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
     * SQL datetime subcode.
     */
    @Column(name = "SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    /**
     * The ordinal position of the parameter/column, starting at 1.
     */
    @Column(name = "ORDINAL_POSITION")
    private int ordinalPosition;

    /**
     * Indicates whether values for this parameter are generated automatically.
     * <ul>
     *     <li>YES           --- if the column is auto incremented
     *     <li>NO            --- if the column is not auto incremented
     *     <li>empty string  --- if it cannot be determined whether the column is auto incremented
     * </ul>
     */
    @Column(name = "IS_AUTOINCREMENT")
    private String isAutoincrement;
}