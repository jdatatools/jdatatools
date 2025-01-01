package com.ainouss.jdatatools.batch.metadata;

import lombok.Data;
import jakarta.persistence.Column;

/**
 * Represents the data returned by {@link java.sql.DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}.
 */
@Data
public class SqlIndexInfo {

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
     * Indicates whether index values are non-unique.
     * <ul>
     * <li>false means that the index values are unique
     * <li>true means that the index values are not unique
     * </ul>
     */
    @Column(name = "NON_UNIQUE")
    private boolean nonUnique;

    /**
     * Index catalog (may be null); null when TYPE is tableIndexStatistic.
     */
    @Column(name = "INDEX_QUALIFIER")
    private String indexQualifier;

    /**
     * Index name; null when TYPE is tableIndexStatistic.
     */
    @Column(name = "INDEX_NAME")
    private String indexName;

    /**
     * Type of index.
     * <ul>
     * <li>tableIndexStatistic - this identifies table statistics that are returned in conjunction with a table's index descriptions
     * <li>tableIndexClustered - this is a clustered index
     * <li>tableIndexHashed - this is a hashed index
     * <li>tableIndexOther - this is some other style of index
     * </ul>
     */
    @Column(name = "TYPE")
    private short type;

    /**
     * Column sequence number within index; zero when TYPE is tableIndexStatistic.
     */
    @Column(name = "ORDINAL_POSITION")
    private short ordinalPosition;

    /**
     * Column name.
     */
    @Column(name = "COLUMN_NAME")
    private String columnName;

    /**
     * Column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic.
     */
    @Column(name = "ASC_OR_DESC")
    private String ascOrDesc;

    /**
     * Cardinality.
     */
    @Column(name = "CARDINALITY")
    private int cardinality;

    /**
     * In pages.
     */
    @Column(name = "PAGES")
    private Integer pages;

    /**
     * Filter condition, if any (may be null).
     */
    @Column(name = "FILTER_CONDITION")
    private String filterCondition;
}