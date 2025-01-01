package com.ainouss.jdatatools.batch.metadata;


import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getTableTypes()}.
 */
@Data
public class SqlTableType {

    /**
     * Table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     */
    @Column(name = "TABLE_TYPE")
    private String tableType;


}