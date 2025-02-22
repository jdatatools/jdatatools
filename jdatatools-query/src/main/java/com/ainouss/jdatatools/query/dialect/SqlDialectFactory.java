package com.ainouss.jdatatools.query.dialect;

public class SqlDialectFactory {

    public static SqlDialect getDialect(String dialectName) {
        if ("mysql".equalsIgnoreCase(dialectName)) {
            return new MySqlDialect();
        } else if ("postgresql".equalsIgnoreCase(dialectName)) {
            return new PostgreSqlDialect();
        } else if ("sqlserver".equalsIgnoreCase(dialectName)) {
            return new SqlServerDialect();
        } else if ("oracle".equalsIgnoreCase(dialectName)) {
            return new OracleDialect();
        } else if ("db2".equalsIgnoreCase(dialectName)) {
            return new DB2Dialect();
        } else if ("sqlite".equalsIgnoreCase(dialectName)) {
            return new SQLiteDialect();
        }
        // Add more dialect options here
        return new StandardDialect(); // Default to standard SQL if dialect not recognized
    }
}