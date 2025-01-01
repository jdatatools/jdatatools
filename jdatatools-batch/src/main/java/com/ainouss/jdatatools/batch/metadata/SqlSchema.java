package com.ainouss.jdatatools.batch.metadata;


import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getSchemas()}.
 */
@Data
public class SqlSchema {

    /**
     * The schema name.
     */
    @Column(name = "TABLE_SCHEM")
    private String tableSchem;

    /**
     * The catalog name to which this schema belongs.
     */
    @Column(name = "TABLE_CATALOG")
    private String tableCatalog;
}