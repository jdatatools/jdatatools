package com.ainouss.jdatatools.batch.mapping;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;

public enum JdbcToJavaTypeMapping {

    // Standard JDBC Types
    BIT(Types.BIT, Boolean.class),
    TINYINT(Types.TINYINT, Byte.class),
    SMALLINT(Types.SMALLINT, Short.class),
    INTEGER(Types.INTEGER, Integer.class),
    BIGINT(Types.BIGINT, Long.class),
    FLOAT(Types.FLOAT, Float.class),
    REAL(Types.REAL, Float.class),
    DOUBLE(Types.DOUBLE, Double.class),
    NUMERIC(Types.NUMERIC, BigDecimal.class),
    DECIMAL(Types.DECIMAL, BigDecimal.class),
    CHAR(Types.CHAR, String.class),
    VARCHAR(Types.VARCHAR, String.class),
    LONGVARCHAR(Types.LONGVARCHAR, String.class),
    DATE(Types.DATE, Date.class),
    TIME(Types.TIME, Time.class),
    TIMESTAMP(Types.TIMESTAMP, Timestamp.class),
    BINARY(Types.BINARY, byte[].class),
    VARBINARY(Types.VARBINARY, byte[].class),
    LONGVARBINARY(Types.LONGVARBINARY, Blob.class),
    NULL(Types.NULL, Object.class),
    OTHER(Types.OTHER, Object.class),
    JAVA_OBJECT(Types.JAVA_OBJECT, Object.class),
    DISTINCT(Types.DISTINCT, Object.class),
    STRUCT(Types.STRUCT, Struct.class),
    ARRAY(Types.ARRAY, java.sql.Array.class),
    BLOB(Types.BLOB, Blob.class),
    CLOB(Types.CLOB, Clob.class),
    REF(Types.REF, java.sql.Ref.class),
    DATALINK(Types.DATALINK, java.net.URL.class),
    BOOLEAN(Types.BOOLEAN, Boolean.class),
    ROWID(Types.ROWID, RowId.class),
    NCHAR(Types.NCHAR, String.class),
    NVARCHAR(Types.NVARCHAR, String.class),
    LONGNVARCHAR(Types.LONGNVARCHAR, String.class),
    NCLOB(Types.NCLOB, NClob.class),
    SQLXML(Types.SQLXML, SQLXML.class),
    REF_CURSOR(Types.REF_CURSOR, java.sql.ResultSet.class),
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE, OffsetTime.class),
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class),

    // Oracle-Specific JDBC Extensions (These are not standard java.sql.Types but often returned by Oracle drivers)
    // Note: The actual integer values might vary slightly depending on the driver version. These are common values.
    // It's generally recommended to rely on standard Types where possible.
    ORACLE_CURSOR(-10, java.sql.ResultSet.class), // Oracle's equivalent of REF_CURSOR
    ORACLE_BFILE(-13, Blob.class), // Binary File stored outside the database
    ORACLE_OBJECT(-101, Struct.class), // Oracle Object Type
    ORACLE_TABLE(-102, java.sql.Array.class), // Oracle TABLE collection
    ORACLE_VARRAY(-103, java.sql.Array.class), // Oracle VARRAY collection
    ORACLE_TIMESTAMP_WITH_LTZ(-1013, OffsetDateTime.class), // Timestamp with local timezone
    ORACLE_TIMESTAMP_WITH_NTZ(-1020, OffsetDateTime.class), // Timestamp with named timezone
    ORACLE_INTERVAL_YM(-104, String.class), // Interval Year to Month
    ORACLE_INTERVAL_DS(-103, String.class), // Interval Day to Second
    ORACLE_UROWID(Types.VARCHAR, String.class), // Usually represented as a String
    // Note: Oracle's NUMBER can map to various Java types depending on precision and scale.
    // The standard NUMERIC and DECIMAL types should cover most cases.
    // If you need more precise mapping, you might handle it based on metadata.

    UNKNOWN(Integer.MIN_VALUE, Object.class);

    private final int jdbcType;
    private final Class<?> javaType;

    JdbcToJavaTypeMapping(int jdbcType, Class<?> javaType) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    private static final Map<Integer, JdbcToJavaTypeMapping> lookup = new HashMap<>();

    static {
        for (JdbcToJavaTypeMapping mapping : JdbcToJavaTypeMapping.values()) {
            lookup.put(mapping.getJdbcType(), mapping);
        }
    }

    public static JdbcToJavaTypeMapping get(int jdbcType) {
        return lookup.getOrDefault(jdbcType, UNKNOWN);
    }

    public static Class<?> getJavaType(int jdbcType) {
        return get(jdbcType).getJavaType();
    }
}