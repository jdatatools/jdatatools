package com.ainouss.jdatatools.batch.metadata;

import lombok.Data;
import jakarta.persistence.Column;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getProcedureColumns(String, String, String, String)}.
 */
@Data
public class SqlProcedureColumn {

    /**
     * Procedure catalog (may be null).
     */
    @Column(name = "PROCEDURE_CAT")
    private String procedureCat;

    /**
     * Procedure schema (may be null).
     */
    @Column(name = "PROCEDURE_SCHEM")
    private String procedureSchem;

    /**
     * Procedure name.
     */
    @Column(name = "PROCEDURE_NAME")
    private String procedureName;

    /**
     * Kind of parameter/column.
     * <ul>
     * <li>procedureColumnUnknown - The kind of parameter is unknown
     * <li>procedureColumnIn - IN parameter
     * <li>procedureColumnInOut - INOUT parameter
     * <li>procedureColumnOut - OUT parameter
     * <li>procedureColumnReturn - Procedure return value
     * <li>procedureColumnResult - Result column in ResultSet
     * </ul>
     */
    @Column(name = "COLUMN_TYPE")
    private short columnType;

    /**
     * Column/parameter name.
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * SQL type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Data source dependent type name, for a UDT it is the fully qualified SQL type name.
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
     * Number of scale.
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
     * <li>procedureNoNulls - Does not allow NULL values
     * <li>procedureNullable - Allows NULL values
     * <li>procedureNullableUnknown - Nullability unknown
     * </ul>
     */
    @Column(name = "NULLABLE")
    private short nullable;

    /**
     * Remarks describing the parameter/column.
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * Reserved for future use.
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
     * The ordinal position of the parameter, starting at 1.
     */
    @Column(name = "ORDINAL_POSITION")
    private int ordinalPosition;

    /**
     * Indicates whether this is a pseudo column like an IDENTITY column.
     *
     * @return "YES" if is a pseudo column, "NO" if not or {@code null} if not known
     */
    @Column(name = "IS_PSEUDO")
    private String isPseudo;

    /**
     * Indicates whether values for this parameter are generated automatically.
     *
     * @return "YES" if the parameter holds values that are generated automatically,
     * "NO" if not or {@code null} if it can't be determined
     */
    @Column(name = "IS_AUTOINCREMENT")
    private String isAutoincrement;
}