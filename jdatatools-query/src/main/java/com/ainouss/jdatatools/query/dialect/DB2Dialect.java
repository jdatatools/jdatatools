package com.ainouss.jdatatools.query.dialect;

public class DB2Dialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" || ", parts); // DB2 uses || for concatenation
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "\"" + identifier + "\""; // DB2 uses double quotes
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "OFFSET " + offset + " ROWS FETCH FIRST " + limit + " ROWS ONLY"; // DB2 syntax
        } else if (limit != null) {
            return "FETCH FIRST " + limit + " ROWS ONLY"; // DB2 limit only
        } else if (offset != null) {
            // DB2 might require row_number() and subquery for offset-only, similar to older Oracle
            // A simplified OFFSET only without LIMIT might not be directly supported in older versions
            return "OFFSET " + offset + " ROWS"; // This might work in some DB2 versions, check compatibility
        }
        return "";
    }
}