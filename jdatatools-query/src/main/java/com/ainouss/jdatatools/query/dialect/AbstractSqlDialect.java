package com.ainouss.jdatatools.query.dialect;

import com.ainouss.jdatatools.query.core.SqlDialect;

public abstract class AbstractSqlDialect implements SqlDialect {

    @Override
    public String getBooleanValue(boolean value) {
        return value ? "true" : "false"; // Default to standard SQL boolean literals
    }

    @Override
    public String getStringConcatenation(String... parts) {
        return "concat(" + String.join(", ", parts) + ")"; // Default to CONCAT function
    }

    @Override
    public String escapeIdentifier(String identifier) {
        return identifier;// Default to no escaping - escape only when necessary in subclasses
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "limit " + limit + " offset " + offset; // Default LIMIT OFFSET
        } else if (limit != null) {
            return "limit " + limit;
        } else if (offset != null) {
            return "offset " + offset; // Some databases might not support OFFSET without LIMIT
        }
        return "";
    }

    @Override
    public String getDistinctKeyword() {
        return "distinct"; // Default DISTINCT keyword
    }
}