package com.ainouss.datatools.jdatatools.util;


import com.ainouss.datatools.jdatatools.query.CriteriaQuery;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class QueryBuilder {

    private static final String SELECT = "select";
    private static final String FROM = "from";

    /**
     * Oracle 12+ offset
     *
     * @param start start
     * @param fetch page size
     * @return offset
     */
    public static String buildOffsetQuery(int start, int fetch) {
        return " offset __START__  rows fetch next __FETCH__ ROWS ONLY"
                .replace("__START__", Integer.toString(start))
                .replace("__FETCH__", Integer.toString(fetch));
    }


    /**
     * List of selectable fields for a given class
     *
     * @param clazz class
     * @return fields
     */
    public static List<Field> getSelectableFields(Class<?> clazz) {
        return Arrays.stream(
                        FieldUtils.getFieldsWithAnnotation(clazz, Column.class)
                )
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    public static String buildCountQuery(String query) {
        String selectIgnoreCase = StringUtils.replaceIgnoreCase(query, SELECT, SELECT);
        String fromIgnoreCase = StringUtils.replaceIgnoreCase(selectIgnoreCase, FROM, FROM);
        String fields = StringUtils.substringBetween(fromIgnoreCase, SELECT, " from ");
        return fromIgnoreCase.replace(fields, " count(*)");
    }

    public static String getTableNameFromInsertQuery(String insert) {
        insert = insert.toLowerCase();
        return StringUtils.substringBetween(insert, " into ", "(").toUpperCase().trim();
    }

    public static String getTableNameFromSelectQuery(String query) {
        query = query.toLowerCase();
        return StringUtils.substringBetween(query, " from ", " ").toUpperCase().trim();
    }


    public static String buildCountQuery(CriteriaQuery<?> cr) {
        String query = cr.buildSelectQuery();
        return buildCountQuery(query);
    }
}
