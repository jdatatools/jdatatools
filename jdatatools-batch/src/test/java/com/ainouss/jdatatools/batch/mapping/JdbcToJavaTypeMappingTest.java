package com.ainouss.jdatatools.batch.mapping;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class JdbcToJavaTypeMappingTest {

    @Test
    void testStandardJdbcTypeMappings() {
        assertEquals(Boolean.class, JdbcToJavaTypeMapping.getJavaType(Types.BIT));
        assertEquals(Boolean.class, JdbcToJavaTypeMapping.getJavaType(Types.BOOLEAN));
        assertEquals(Byte.class, JdbcToJavaTypeMapping.getJavaType(Types.TINYINT));
        assertEquals(Short.class, JdbcToJavaTypeMapping.getJavaType(Types.SMALLINT));
        assertEquals(Integer.class, JdbcToJavaTypeMapping.getJavaType(Types.INTEGER));
        assertEquals(Long.class, JdbcToJavaTypeMapping.getJavaType(Types.BIGINT));
        assertEquals(Float.class, JdbcToJavaTypeMapping.getJavaType(Types.FLOAT));
        assertEquals(Float.class, JdbcToJavaTypeMapping.getJavaType(Types.REAL));
        assertEquals(Double.class, JdbcToJavaTypeMapping.getJavaType(Types.DOUBLE));
        assertEquals(java.math.BigDecimal.class, JdbcToJavaTypeMapping.getJavaType(Types.NUMERIC));
        assertEquals(java.math.BigDecimal.class, JdbcToJavaTypeMapping.getJavaType(Types.DECIMAL));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.CHAR));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.VARCHAR));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.LONGVARCHAR));
        assertEquals(java.sql.Date.class, JdbcToJavaTypeMapping.getJavaType(Types.DATE));
        assertEquals(java.sql.Time.class, JdbcToJavaTypeMapping.getJavaType(Types.TIME));
        assertEquals(java.sql.Timestamp.class, JdbcToJavaTypeMapping.getJavaType(Types.TIMESTAMP));
        assertEquals(byte[].class, JdbcToJavaTypeMapping.getJavaType(Types.BINARY));
        assertEquals(byte[].class, JdbcToJavaTypeMapping.getJavaType(Types.VARBINARY));
        assertEquals(java.sql.Blob.class, JdbcToJavaTypeMapping.getJavaType(Types.LONGVARBINARY));
        assertEquals(Object.class, JdbcToJavaTypeMapping.getJavaType(Types.NULL));
        assertEquals(Object.class, JdbcToJavaTypeMapping.getJavaType(Types.OTHER));
        assertEquals(Object.class, JdbcToJavaTypeMapping.getJavaType(Types.JAVA_OBJECT));
        assertEquals(Object.class, JdbcToJavaTypeMapping.getJavaType(Types.DISTINCT));
        assertEquals(java.sql.Struct.class, JdbcToJavaTypeMapping.getJavaType(Types.STRUCT));
        assertEquals(java.sql.Array.class, JdbcToJavaTypeMapping.getJavaType(Types.ARRAY));
        assertEquals(java.sql.Blob.class, JdbcToJavaTypeMapping.getJavaType(Types.BLOB));
        assertEquals(java.sql.Clob.class, JdbcToJavaTypeMapping.getJavaType(Types.CLOB));
        assertEquals(java.sql.Ref.class, JdbcToJavaTypeMapping.getJavaType(Types.REF));
        assertEquals(java.net.URL.class, JdbcToJavaTypeMapping.getJavaType(Types.DATALINK));
        assertEquals(java.sql.RowId.class, JdbcToJavaTypeMapping.getJavaType(Types.ROWID));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.NCHAR));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.NVARCHAR));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.LONGNVARCHAR));
        assertEquals(java.sql.NClob.class, JdbcToJavaTypeMapping.getJavaType(Types.NCLOB));
        assertEquals(java.sql.SQLXML.class, JdbcToJavaTypeMapping.getJavaType(Types.SQLXML));
        assertEquals(java.sql.ResultSet.class, JdbcToJavaTypeMapping.getJavaType(Types.REF_CURSOR));
        assertEquals(java.time.OffsetTime.class, JdbcToJavaTypeMapping.getJavaType(Types.TIME_WITH_TIMEZONE));
        assertEquals(java.time.OffsetDateTime.class, JdbcToJavaTypeMapping.getJavaType(Types.TIMESTAMP_WITH_TIMEZONE));
    }

    @Test
    void testOracleSpecificMappings() {
        assertEquals(java.sql.ResultSet.class, JdbcToJavaTypeMapping.ORACLE_CURSOR.getJavaType());
        assertEquals(java.sql.Blob.class, JdbcToJavaTypeMapping.ORACLE_BFILE.getJavaType());
        assertEquals(java.sql.Struct.class, JdbcToJavaTypeMapping.ORACLE_OBJECT.getJavaType());
        assertEquals(java.sql.Array.class, JdbcToJavaTypeMapping.ORACLE_TABLE.getJavaType());
        assertEquals(java.sql.Array.class, JdbcToJavaTypeMapping.ORACLE_VARRAY.getJavaType());
        assertEquals(java.time.OffsetDateTime.class, JdbcToJavaTypeMapping.ORACLE_TIMESTAMP_WITH_LTZ.getJavaType());
        assertEquals(java.time.OffsetDateTime.class, JdbcToJavaTypeMapping.ORACLE_TIMESTAMP_WITH_NTZ.getJavaType());
        assertEquals(String.class, JdbcToJavaTypeMapping.ORACLE_INTERVAL_YM.getJavaType());
        assertEquals(String.class, JdbcToJavaTypeMapping.ORACLE_INTERVAL_DS.getJavaType());
        assertEquals(String.class, JdbcToJavaTypeMapping.ORACLE_UROWID.getJavaType());
    }

    @Test
    void testGetJavaTypeMethod() {
        assertEquals(Integer.class, JdbcToJavaTypeMapping.getJavaType(Types.INTEGER));
        assertEquals(String.class, JdbcToJavaTypeMapping.getJavaType(Types.VARCHAR));
        assertEquals(java.sql.ResultSet.class, JdbcToJavaTypeMapping.getJavaType(JdbcToJavaTypeMapping.ORACLE_CURSOR.getJdbcType()));
    }

    @Test
    void testUnknownMapping() {
        assertEquals(Object.class, JdbcToJavaTypeMapping.getJavaType(Integer.MIN_VALUE));
        assertEquals(JdbcToJavaTypeMapping.UNKNOWN, JdbcToJavaTypeMapping.get(Integer.MIN_VALUE));
    }
}