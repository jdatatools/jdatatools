package com.ainouss.jdatatools.batch.metadata;

import lombok.Data;
import jakarta.persistence.Column;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getProcedures(String, String, String)}.
 */
@Data
public class SqlProcedure {

    /**
     * Procedure catalog (may be null).
     */
    @Column(name = "PROCEDURE_CAT")
    private String procedureCat;

    /**
     * Procedure schema (may be null).
     */
    @Column(name = "PROCEDURE_SCHEM")
    private String procedureSchem;

    /**
     * Procedure name.
     */
    @Column(name = "PROCEDURE_NAME")
    private String procedureName;

    /**
     * Reserved for future use.
     */
    @Column(name = "reserved1")
    private String reserved1;

    /**
     * Reserved for future use.
     */
    @Column(name = "reserved2")
    private String reserved2;

    /**
     * Reserved for future use.
     */
    @Column(name = "reserved3")
    private String reserved3;

    /**
     * Explanatory comment on the procedure.
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * Kind of procedure.
     * <ul>
     * <li>procedureResultUnknown - May return a result
     * <li>procedureNoResult - Does not return a result
     * <li>procedureReturnsResult - Returns a result
     * </ul>
     */
    @Column(name = "PROCEDURE_TYPE")
    private short procedureType;

    /**
     * Specific name of this procedure (may be null).
     */
    @Column(name = "SPECIFIC_NAME")
    private String specificName;
}