package com.ainouss.datatools.transfer.store;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DataSourceRegistry {

    INSTANCE;

    private final Map<String, DataSource> dataSources = new HashMap<>();

    public DataSource getDataSource(String name) {
        return dataSources.get(name);
    }

    public List<DataSource> getDataSources() {
        return new ArrayList<>(dataSources.values());
    }

    public void createDataSource(String dbName, String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName(dbName + "-pool"); // Give the pool a meaningful name
        // Additional HikariCP configurations (optional):
        config.setMinimumIdle(5);             // Minimum number of idle connections
        config.setMaximumPoolSize(20);       // Maximum number of connections in the pool
        config.setConnectionTimeout(30000);  // Connection timeout in milliseconds
        config.setIdleTimeout(600000);      // Idle timeout in milliseconds
        config.setMaxLifetime(1800000);     // Maximum lifetime of a connection in milliseconds
        // Add more configurations as needed (see HikariCP documentation)
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        dataSources.put(dbName, hikariDataSource);
    }

}