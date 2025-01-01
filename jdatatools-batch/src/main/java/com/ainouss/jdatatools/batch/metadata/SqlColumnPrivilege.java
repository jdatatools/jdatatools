package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.DatabaseMetaData;

/**
 * Represents the data returned by
 * {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}.
 */
@Data
public class SqlColumnPrivilege {

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
     * Grantor of access (may be null).
     */
    @Column(name = "GRANTOR")
    private String grantor;

    /**
     * Grantee of access.
     */
    @Column(name = "GRANTEE")
    private String grantee;

    /**
     * Privilege granted.
     */
    @Column(name = "PRIVILEGE")
    private String privilege;

    /**
     * "YES" if grantee is permitted to grant to others; "NO" if not;
     * {@code null} if unknown.
     */
    @Column(name = "IS_GRANTABLE")
    private String isGrantable;
}