package com.ainouss.jdatatools.query.dialect;

public class SqlServerDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" + ", parts); // SQL Server uses + for concatenation (in many contexts)
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "[" + identifier + "]"; // SQL Server uses square brackets
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "OFFSET " + offset + " ROWS FETCH NEXT " + limit + " ROWS ONLY"; // SQL Server specific syntax
        } else if (limit != null) {
            return "TOP " + limit; // TOP for just limit in SQL Server
        } else if (offset != null) {
            return "OFFSET " + offset + " ROWS"; // OFFSET without LIMIT in SQL Server
        }
        return "";
    }
}