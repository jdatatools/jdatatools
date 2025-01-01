package com.ainouss.datatools.transfer.core;

import com.ainouss.jdatatools.batch.mapping.JdbcToJavaTypeMapping;
import com.ainouss.jdatatools.batch.metadata.SqlColumn;
import com.ainouss.jdatatools.batch.metadata.SqlTable;
import com.ainouss.jdatatools.batch.metadata.SqlTableTypes;
import com.ainouss.jdatatools.query.registery.EntityRegistry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class JavaClassGenerator {

    private final DatabaseMetadataReader metaReader;

    public void generate(DataSource dataSource, String pkg) {

        metaReader.getTables(dataSource, null, null, null, List.of(SqlTableTypes.TABLE.name()))
                .forEach(table -> {
                    Class<?> clazz = makeAndLoad(pkg, table, dataSource);
                    EntityRegistry.registerClass(clazz);
                });
    }

    private Class<?> makeAndLoad(String pkg, SqlTable table, DataSource dataSource) {
        try {
            DynamicType.Unloaded<?> unloaded = make(table.getTableName(), pkg, dataSource);
            return unloaded.load(getClass().getClassLoader()).getLoaded();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private DynamicType.Unloaded<?> make(String tableName, String pkg, DataSource dataSource) throws SQLException {

        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class)
                .name(pkg + "." + tableNameToPojo(tableName))
                .annotateType(AnnotationDescription.Builder.ofType(Entity.class).build())
                .annotateType(AnnotationDescription.Builder.ofType(Table.class)
                        .define("name", tableName)
                        .build())
                .annotateType(AnnotationDescription.Builder.ofType(Data.class).build());

        Collection<SqlColumn> columnList = metaReader.getColumns(dataSource, null, null, tableName, null);
        for (SqlColumn column : columnList) {
            Class<?> javaType = JdbcToJavaTypeMapping.getJavaType(column.getDataType());
            String field = columnNameToFieldName(column.getColumnName());
            builder = builder.defineField(field, javaType, Modifier.PRIVATE)
                    .annotateField(AnnotationDescription.Builder.ofType(Column.class)
                            .define("name", column.getColumnName())
                            .build());
        }
        return builder.make();
    }

    public static String columnNameToFieldName(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return columnName;
        }

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]+");
        String[] parts = pattern.split(columnName);

        if (parts.length == 0) {
            return columnName; // Should not happen with the regex, but for safety
        }

        // Handle the first part, converting it to lowercase
        String firstPart = parts[0].toLowerCase(Locale.ENGLISH);

        // Process the remaining parts, capitalizing the first letter
        String remainingParts = Arrays.stream(parts, 1, parts.length)
                .map(part -> part.substring(0, 1).toUpperCase(Locale.ENGLISH) + part.substring(1).toLowerCase(Locale.ENGLISH))
                .collect(Collectors.joining());

        return firstPart + remainingParts;
    }

    private String tableNameToPojo(String text) {
        String str = columnNameToFieldName(text);
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}