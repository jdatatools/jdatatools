package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getSuperTables(String, String, String)}.
 */
@Data
public class SqlSuperTable {

    /**
     * The catalog of the table.
     */
    @Column(name = "TABLE_CAT")
    private String tableCat;

    /**
     * The schema of the table.
     */
    @Column(name = "TABLE_SCHEM")
    private String tableSchem;

    /**
     * The name of the table.
     */
    @Column(name = "TABLE_NAME")
    private String tableName;

    /**
     * The catalog of the direct supertable (may be null).
     */
    @Column(name = "SUPERTABLE_NAME")
    private String supertableName;
}
