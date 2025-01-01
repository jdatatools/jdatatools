package com.ainouss.jdatatools.batch.reader;

import com.ainouss.jdatatools.batch.mapping.JdbcToJavaTypeMapping;
import com.ainouss.jdatatools.query.core.FieldMetaData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static com.ainouss.jdatatools.query.util.DataUtils.isBlank;
import static com.ainouss.jdatatools.query.util.DataUtils.trimToNull;

@Slf4j
@Component
public class RowExtractor {

    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static DateTimeFormatter dtmf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RowExtractor() {
    }

    public static <R> List<R> asList(ResultSet resultSet, Class<R> clazz) throws SQLException {
        List<R> data = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<FieldMetaData> fields = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            FieldMetaData fieldMetaData = new FieldMetaData();
            fieldMetaData.setLabel(metaData.getColumnLabel(i));
            fieldMetaData.setColumn(metaData.getColumnName(i));
            fieldMetaData.setJavaType(JdbcToJavaTypeMapping.getJavaType(metaData.getColumnType(i)));
            fields.add(fieldMetaData);
        }
        while (resultSet.next()) {
            data.add(extract(resultSet, clazz, fields, null, uuid));
        }
        resultSet.close();
        return data;
    }

    /**
     * Extract a result set row, into a given class
     *
     * @param <T>           type
     * @param resultSet     SQL resultSet
     * @param clazz         target
     * @param fields        fields
     * @param correlationId correlation id
     * @return record
     */
    public static <T, R> R extract(ResultSet resultSet, Class<T> clazz, List<FieldMetaData> fields, Function<T, R> mapper, String correlationId) {
        T instance = instantiate(clazz);
        for (FieldMetaData field : fields) {
            try {
                Object value = extractField(resultSet, field.getColumn(), field.getJavaType(), correlationId);
                if (value != null) {
                    FieldUtils.writeField(instance.getClass().getDeclaredField(field.getLabel()), instance, value, true);
                }
            } catch (SQLException e) {
                log.warn("ID-{} received an invalid value for {}, exception {}", correlationId, field.getLabel(), e.getMessage());
            } catch (IllegalAccessException | IllegalArgumentException e) {
                log.warn("ID-{} could not write value for {}, exception {}", correlationId, field.getLabel(), e.getMessage());
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        if (mapper == null) {
            return (R) instance;
        }
        return mapper.apply(instance);
    }

    /**
     * Instantiate a class
     *
     * @param clazz target
     * @param <T>   type
     * @return instance
     */
    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Could not instantiate class {}, make sure to include a public constructor " + clazz.getSimpleName());
        }
    }

    /**
     * Extract a field value
     *
     * @param resultSet     current resultset
     * @param correlationId correlation id
     * @return Value of the field as object
     */
    public static Object extractField(ResultSet resultSet, String name, Class<?> fieldType, String correlationId) throws SQLException {
        if (fieldType.equals(String.class)) {
            return trimToNull(resultSet.getString(name));
        } else if (fieldType.equals(Double.class)) {
            return getaDouble(resultSet, name);
        } else if (fieldType.equals(LocalDateTime.class)) {
            return getLocalDateTime(resultSet, name, correlationId);
        } else if (fieldType.equals(Date.class)) {
            return getSqlDate(resultSet, name, correlationId);
        } else if (fieldType.equals(Time.class)) {
            return getSqlTime(resultSet, name, correlationId);
        } else if (fieldType.equals(Timestamp.class)) {
            return getSqlTimeStamp(resultSet, name, correlationId);
        } else if (fieldType.equals(LocalDate.class)) {
            return getLocalDate(resultSet, name, correlationId);
        } else if (fieldType.equals(Long.class)) {
            return getLong(resultSet, name);
        } else if (fieldType.equals(BigDecimal.class)) {
            return getBigDecimal(resultSet, name);
        } else if (fieldType.equals(Boolean.class)) {
            return getBoolean(resultSet, name);
        } else if (fieldType.equals(Integer.class)) {
            return getInteger(resultSet, name);
        } else if (fieldType.equals(Float.class)) {
            return getFloat(resultSet, name);
        } else if (fieldType.equals(byte[].class)) {
            return getBytes(resultSet, name);
        }
        return resultSet.getObject(name);
    }


    /**
     * Extract Float from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */

    private static Float getFloat(ResultSet resultSet, String name) throws SQLException {
        var f = resultSet.getFloat(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return f;
    }

    private static byte[] getBytes(ResultSet resultSet, String name) throws SQLException {
        var b = resultSet.getBytes(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return b;
    }

    /**
     * Extract Integer from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */

    private static Integer getInteger(ResultSet resultSet, String name) throws SQLException {
        var i = resultSet.getInt(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return i;
    }

    /**
     * Extract Boolean from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */

    private static Boolean getBoolean(ResultSet resultSet, String name) throws SQLException {
        var b = resultSet.getBoolean(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return b;
    }

    /**
     * Extract BigDecimal from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */

    private static BigDecimal getBigDecimal(ResultSet resultSet, String name) throws SQLException {
        BigDecimal b = resultSet.getBigDecimal(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return b;
    }

    /**
     * Extract Long from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */
    private static Long getLong(ResultSet resultSet, String name) throws SQLException {
        long l = resultSet.getLong(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return l;
    }

    /**
     * Extract Double from ResultSet
     *
     * @param resultSet result set
     * @param name      field name
     * @return LocalDate
     * @throws SQLException on fail
     */
    private static Double getaDouble(ResultSet resultSet, String name) throws SQLException {
        double d = resultSet.getDouble(name);
        if (resultSet.wasNull()) {
            return null;
        }
        return d;
    }

    /**
     * Extract local date time from ResultSet
     *
     * @param resultSet     result set
     * @param name          field name
     * @param correlationId correlation ID
     * @return LocalDate
     * @throws SQLException on fail
     */
    private static LocalDateTime getLocalDateTime(ResultSet resultSet, String name, String correlationId) throws SQLException {
        Date date;
        try {
            date = resultSet.getDate(name);
            if (date == null) {
                return null;
            }
            long time = date.getTime();
            return Instant.ofEpochMilli(time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (SQLException e) {
            String strDate = resultSet.getString(name);
            log.error("ID-{} could not extract value for field {} of type LocalDateTime with value : {}, error :{} ", correlationId, name, strDate, e.getMessage());
            return parseLocalDateTime(strDate);
        }
    }

    /**
     * Extract SQL date time from ResultSet
     *
     * @param resultSet     result set
     * @param name          field name
     * @param correlationId correlation ID
     * @return java.sql.Date
     * @throws SQLException on fail
     */
    private static Date getSqlDate(ResultSet resultSet, String name, String correlationId) throws SQLException {
        try {
            return resultSet.getDate(name);
        } catch (SQLException e) {
            String strDate = resultSet.getString(name);
            log.error("ID-{} could not extract value for field {} of type Date with value : {}, error :{} ", correlationId, name, strDate, e.getMessage());
            return Date.valueOf(strDate);
        }
    }

    /**
     * Extract SQL time from ResultSet
     *
     * @param resultSet     result set
     * @param name          field name
     * @param correlationId correlation ID
     * @return java.sql.Date
     * @throws SQLException on fail
     */
    private static Time getSqlTime(ResultSet resultSet, String name, String correlationId) throws SQLException {
        Time date;
        try {
            date = resultSet.getTime(name);
            return date;
        } catch (SQLException e) {
            String strDate = resultSet.getString(name);
            log.error("ID-{} could not extract value for field {} of type Time with value : {}, error :{} ", correlationId, name, strDate, e.getMessage());
            return Time.valueOf(strDate);
        }
    }

    /**
     * Extract local date timestamp from ResultSet
     *
     * @param resultSet     result set
     * @param name          field name
     * @param correlationId correlation ID
     * @return java.sql.Date
     * @throws SQLException on fail
     */
    private static Timestamp getSqlTimeStamp(ResultSet resultSet, String name, String correlationId) throws SQLException {
        Timestamp date;
        try {
            date = resultSet.getTimestamp(name);
            return date;
        } catch (SQLException e) {
            String strDate = resultSet.getString(name);
            log.error("ID-{} could not extract value for field {} of type Timestamp with value : {}, error :{} ", correlationId, name, strDate, e.getMessage());
            return Timestamp.valueOf(strDate);
        }
    }

    /**
     * Extract local date from ResultSet
     *
     * @param resultSet     result set
     * @param name          field name
     * @param correlationId correlation id
     * @return LocalDate
     * @throws SQLException on fail
     */
    public static LocalDate getLocalDate(ResultSet resultSet, String name, String correlationId) throws SQLException {
        Date date;
        try {
            date = resultSet.getDate(name);
            return date == null ? null : date.toLocalDate();
        } catch (SQLException e) {
            String strDate = resultSet.getString(name);
            log.error("ID-{} could not extract value for field {} of type LocalDate with value : {}, error :{} ", correlationId, name, strDate, e.getMessage());
            return parseLocalDate(strDate);
        }
    }

    /**
     * Read date time
     *
     * @param strDate       date string
     * @return date time, nullable
     */
    public static LocalDateTime parseLocalDateTime(String strDate) {
        try {
            return LocalDateTime.parse(strDate, dtmf);
        } catch (DateTimeParseException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * Read date
     *
     * @param strDate       date string
     * @return LocalDate, nullable
     */

    public static LocalDate parseLocalDate(String strDate) {
        if (isBlank(strDate)) {
            return null;
        }
        try {
            return LocalDate.parse(strDate, dtf);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }

}
