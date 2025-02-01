package com.ainouss.jdatatools.query.dialect;

public interface SqlDialect {

    /**
     * Gets the SQL representation for limiting query results with limit and offset.
     *
     * @param limit  The maximum number of rows to return.
     * @param offset The starting offset of the first row to return.
     * @return SQL fragment for LIMIT and OFFSET clauses.
     */
    String getLimitOffsetSql(Integer limit, Integer offset);

    /**
     * Gets the SQL representation for a boolean value.
     *
     * @param value The boolean value.
     * @return SQL string for boolean (e.g., 'TRUE', 'FALSE', '1', '0').
     */
    String getBooleanValue(boolean value);

    /**
     * Gets the SQL representation for string concatenation.
     *
     * @param parts Strings to concatenate.
     * @return SQL string for concatenation.
     */
    String getStringConcatenation(String... parts);

    /**
     * Escapes an identifier (table name, column name) for the specific dialect.
     *
     * @param identifier The identifier to escape.
     * @return Escaped identifier.
     */
    String escapeIdentifier(String identifier);

    /**
     * Gets the SQL keyword for 'DISTINCT' keyword, some dialects may require specific syntax.
     * @return SQL keyword for DISTINCT.
     */
    String getDistinctKeyword();
}