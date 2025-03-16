package com.ainouss.jdatatools.query.dialect;

public class OracleDialect extends AbstractSqlDialect {

    @Override
    public String getStringConcatenation(String... parts) {
        return String.join(" || ", parts);
    }

    @Override
    public String escapeIdentifier(String identifier) {
        String[] parts = identifier.split("\\.");
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) escaped.append(".");
            escaped.append("\"").append(parts[i]).append("\"");
        }
        return escaped.toString();
    }

    @Override
    public String getLimitOffsetSql(Integer limit, Integer offset) {
        if (limit != null && offset != null) {
            return "offset " + offset + " rows fetch next " + limit + " rows only";
        } else if (limit != null) {
            return "fetch first " + limit + " rows only";
        } else if (offset != null) {
            return "offset " + offset + " rows";
        }
        return "";
    }
}