package com.ainouss.jdatatools.query.dialect;

public class MySqlDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return "CONCAT(" + String.join(", ", parts) + ")"; // MySQL uses CONCAT
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "`" + identifier + "`"; // MySQL uses backticks for identifiers
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "LIMIT " + limit + " OFFSET " + offset; // MySQL standard LIMIT OFFSET
        } else if (limit != null) {
            return "LIMIT " + limit;
        } else if (offset != null) {
            return "OFFSET " + offset; // MySQL supports OFFSET without LIMIT (though not standard SQL)
        }
        return "";
    }
}