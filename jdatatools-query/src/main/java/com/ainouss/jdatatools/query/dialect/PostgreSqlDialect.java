package com.ainouss.jdatatools.query.dialect;

public class PostgreSqlDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" || ", parts); // PostgreSQL uses || for concatenation
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return "\"" + identifier + "\""; // PostgreSQL uses double quotes (standard SQL)
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        return super.getLimitOffsetSql(limit, offset); // Uses default LIMIT OFFSET, which PostgreSQL supports
    }
}