package com.ainouss.jdatatools.batch.metadata;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getFunctions(String, String, String)}.
 */
@Data
public class SqlFunction {

    /**
     * Function catalog (may be null).
     */
    @Column(name = "FUNCTION_CAT")
    private String functionCat;

    /**
     * Function schema (may be null).
     */
    @Column(name = "FUNCTION_SCHEM")
    private String functionSchem;

    /**
     * Function name.
     */
    @Column(name = "FUNCTION_NAME")
    private String functionName;

    /**
     * Remarks about the function.
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * Kind of function.
     * <ul>
     *     <li>functionResultUnknown - Cannot determine if a return value or table will be returned
     *     <li>functionNoTable - Does not return a table
     *     <li>functionReturnsTable - Returns a table
     * </ul>
     */
    @Column(name = "FUNCTION_TYPE")
    private short functionType;

    /**
     * Specific name of this function (may be null).
     */
    @Column(name = "SPECIFIC_NAME")
    private String specificName;
}