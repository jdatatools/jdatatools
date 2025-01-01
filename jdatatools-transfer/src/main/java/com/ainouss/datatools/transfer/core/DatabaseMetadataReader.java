package com.ainouss.datatools.transfer.core;

import com.ainouss.jdatatools.batch.metadata.SqlColumn;
import com.ainouss.jdatatools.batch.metadata.SqlTable;
import com.ainouss.jdatatools.batch.reader.RowExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class DatabaseMetadataReader {


    public List<SqlTable> getTables(DataSource dataSource, String catalog, String schemaPattern, String tableNamePattern, List<String> types) {
        if (tableNamePattern == null) {
            tableNamePattern = "%";
        }
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types.toArray(new String[0]));
            return RowExtractor.asList(resultSet, SqlTable.class);
        } catch (SQLException e) {
            log.error("Error listing tables: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<SqlColumn> getColumns(DataSource dataSource, String catalog, String schemaPattern, String tableNamePattern,String columnNamePattern){
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet resultSet = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
            return RowExtractor.asList(resultSet, SqlColumn.class);
        } catch (SQLException e) {
            log.error("Error listing columns: {} ", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
