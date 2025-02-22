package com.ainouss.jdatatools.query.dialect;

public class OracleDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" || ", parts); // Oracle uses || for concatenation
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "\"" + identifier + "\""; // Oracle uses double quotes
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY"; // Oracle 12c+ syntax
        } else if (limit != null) {
            return "FETCH FIRST " + limit + " ROWS ONLY"; // Oracle 12c+ for limit only
        } else if (offset != null) {
            // For older versions or when only offset is needed (less common), may require row_number() and subquery
            // A simpler approach, though less performant for large offsets, is using a subquery with ROWNUM
            return "WHERE ROWNUM > " + offset; // Be cautious with this for large offsets in Oracle
        }
        return "";
    }
}