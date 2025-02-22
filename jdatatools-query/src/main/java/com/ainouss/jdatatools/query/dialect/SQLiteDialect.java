package com.ainouss.jdatatools.query.dialect;

public class SQLiteDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" || ", parts); // SQLite uses || for concatenation
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "`" + identifier + "`"; // SQLite uses backticks
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "LIMIT " + limit + " OFFSET " + offset; // SQLite standard LIMIT OFFSET
        } else if (limit != null) {
            return "LIMIT " + limit; // SQLite limit only
        } else if (offset != null) {
            return "OFFSET " + offset; // SQLite offset only
        }
        return "";
    }

    @Override
    public String getBooleanValue(boolean value) {
        return value ? "1" : "0"; // SQLite uses 1 and 0 for booleans
    }
}