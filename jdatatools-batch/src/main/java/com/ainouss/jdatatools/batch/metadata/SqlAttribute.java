package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getAttributes(String, String, String, String)}.
 */
@Data
public class SqlAttribute {

    /**
     * The type catalog being described.
     */
    @Column(name = "TYPE_CAT")
    private String typeCat;

    /**
     * The type schema being described.
     */
    @Column(name = "TYPE_SCHEM")
    private String typeSchem;

    /**
     * The type name being described.
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * The attribute name.
     */
    @Column(name = "ATTR_NAME")
    private String attrName;

    /**
     * The attribute type SQL type from java.sql.Types.
     */
    @Column(name = "DATA_TYPE")
    private int dataType;

    /**
     * The attribute type name.
     */
    @Column(name = "ATTR_TYPE_NAME")
    private String attrTypeName;

    /**
     * The attribute column size.
     */
    @Column(name = "ATTR_SIZE")
    private int attrSize;

    /**
     * Not used.
     */
    @Column(name = "DECIMAL_DIGITS")
    private Integer decimalDigits;

    /**
     * Radix for this attribute.
     */
    @Column(name = "NUM_PREC_RADIX")
    private Integer numPrecRadix;

    /**
     * Indicates whether NULL is allowed.
     * <ul>
     * <li>attributeNoNulls - might not allow NULL values
     * <li>attributeNullable - definitely allows NULL values
     * <li>attributeNullableUnknown - nullability unknown
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
     * Default value for the attribute, which should be interpreted as a string when the value is enclosed in single quotes (may be null).
     */
    @Column(name = "ATTR_DEF")
    private String attrDef;

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
     * The ordinal position of the attribute in the UDT.
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
}