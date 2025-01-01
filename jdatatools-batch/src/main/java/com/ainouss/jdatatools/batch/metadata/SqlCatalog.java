package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getCatalogs()}.
 */
@Data
public class SqlCatalog {

    /**
     * The catalog name.
     */
    @Column(name = "TABLE_CAT")
    private String tableCat;
}