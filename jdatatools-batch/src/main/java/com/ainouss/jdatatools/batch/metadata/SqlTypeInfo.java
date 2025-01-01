package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getTypeInfo()}.
 */
@Data
public class SqlTypeInfo {

    /**
     * Type name.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * Data type SQL type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * Maximum precision.
     */
    @Column(name = "PRECISION")
    private int precision;

    /**
     * Literal prefix used to quote a literal of this data type.
     */
    @Column(name = "LITERAL_PREFIX")
    private String literalPrefix;

    /**
     * Literal suffix used to quote a literal of this data type.
     */
    @Column(name = "LITERAL_SUFFIX")
    private String literalSuffix;

    /**
     * The name used for CREATE TABLE statements.
     */
    @Column(name = "CREATE_PARAMS")
    private String createParams;

    /**
     * Can you use NULL for this type.
     * <ul>
     * <li>typeNoNulls - does not allow NULL values
     * <li>typeNullable - allows NULL values
     * <li>typeNullableUnknown - nullability unknown
     * </ul>
     */
    @Column(name = "NULLABLE")
    private short nullable;

    /**
     * Case sensitivity.
     * <ul>
     * <li>typePredNone - No support for WHERE .. LIKE
     * <li>typePredChar - Support for WHERE .. LIKE
     * <li>typePredBasic - Support for WHERE .. LIKE
     * <li>typeSearchable - Support for WHERE .. LIKE
     * </ul>
     */
    @Column(name = "CASE_SENSITIVE")
    private boolean caseSensitive;

    /**
     * Can be used in a WHERE clause.
     * <ul>
     * <li>typePredNone - No support for WHERE clause
     * <li>typePredChar - Can be used in 'WHERE' clause with 'LIKE' predicate
     * <li>typePredBasic - Supported for 'WHERE' clause, except 'LIKE' predicate
     * <li>typeSearchable - Can be used in 'WHERE' clause with all predicates
     * </ul>
     */
    @Column(name = "SEARCHABLE")
    private short searchable;

    /**
     * Is it unsigned.
     */
    @Column(name = "UNSIGNED_ATTRIBUTE")
    private Boolean unsignedAttribute;

    /**
     * Can it be a money value.
     */
    @Column(name = "FIXED_PREC_SCALE")
    private boolean fixedPrecScale;

    /**
     * Can be used for auto-increment value.
     */
    @Column(name = "AUTO_INCREMENT")
    private Boolean autoIncrement;

    /**
     * Localized version of type name (may be null).
     */
    @Column(name = "LOCAL_TYPE_NAME")
    private String localTypeName;

    /**
     * Minimum scale supported.
     */
    @Column(name = "MINIMUM_SCALE")
    private Short minimumScale;

    /**
     * Maximum scale supported.
     */
    @Column(name = "MAXIMUM_SCALE")
    private Short maximumScale;

    /**
     * SQL Data Type used.
     */
    @Column(name = "SQL_DATA_TYPE")
    private int sqlDataType;

    /**
     * SQL datetime or interval subcode.
     */
    @Column(name = "SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    /**
     * Default precision different than precision (usually applies to LOBs).
     */
    @Column(name = "NUM_PREC_RADIX")
    private Integer numPrecRadix;
}