package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)}.
 */
@Data
public class SqlImportedKey {

    /**
     * Primary key table catalog (may be null).
     */
    @Column(name = "PKTABLE_CAT")
    private String pktableCat;

    /**
     * Primary key table schema (may be null).
     */
    @Column(name = "PKTABLE_SCHEM")
    private String pktableSchem;

    /**
     * Primary key table name.
     */
    @Column(name = "PKTABLE_NAME")
    private String pktableName;

    /**
     * Primary key column name.
     */
    @Column(name = "PKCOLUMN_NAME")
    private String pkcolumnName;

    /**
     * Foreign key table catalog (may be null).
     */
    @Column(name = "FKTABLE_CAT")
    private String fktableCat;

    /**
     * Foreign key table schema (may be null).
     */
    @Column(name = "FKTABLE_SCHEM")
    private String fktableSchem;

    /**
     * Foreign key table name.
     */
    @Column(name = "FKTABLE_NAME")
    private String fktableName;

    /**
     * Foreign key column name.
     */
    @Column(name = "FKCOLUMN_NAME")
    private String fkcolumnName;

    /**
     * Sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 represents the second column within the foreign key).
     */
    @Column(name = "KEY_SEQ")
    private short keySeq;

    /**
     * What happens to foreign key when primary key is updated.
     * <ul>
     * <li>importedNoAction - do not allow update of primary key if it has been imported
     * <li>importedKeyCascade - change imported key to agree with primary key update
     * <li>importedKeySetNull - change imported key to SQL NULL if its primary key has been updated
     * <li>importedKeySetDefault - change imported key to default values if its primary key has been updated
     * <li>importedKeyInitiallyDeferred - see SQL92 for definition
     * <li>importedKeyInitiallyImmediate - see SQL92 for definition
     * <li>importedKeyNotDeferrable - see SQL92 for definition
     * </ul>
     */
    @Column(name = "UPDATE_RULE")
    private short updateRule;

    /**
     * What happens to the foreign key when primary key is deleted.
     * <ul>
     * <li>importedKeyNoAction - do not allow delete of primary key if it has been imported
     * <li>importedKeyCascade - delete rows that import a deleted key
     * <li>importedKeySetNull - change imported key to SQL NULL if its primary key has been deleted
     * <li>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility)
     * <li>importedKeySetDefault - change imported key to default if its primary key has been deleted
     * </ul>
     */
    @Column(name = "DELETE_RULE")
    private short deleteRule;

    /**
     * Foreign key name (may be null).
     */
    @Column(name = "FK_NAME")
    private String fkName;

    /**
     * Primary key name (may be null).
     */
    @Column(name = "PK_NAME")
    private String pkName;

    /**
     * SQLSTATE that indicates when this foreign key constraint is (or is not) initially deferred.
     *
     * @return SQLSTATE for constraint initialization
     * @see #importedKeyInitiallyDeferred
     * @see #importedKeyInitiallyImmediate
     */
    @Column(name = "DEFERRABILITY")
    private short deferrability;
}