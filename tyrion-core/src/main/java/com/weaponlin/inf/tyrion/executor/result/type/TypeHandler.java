package com.weaponlin.inf.tyrion.executor.result.type;

import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

public class TypeHandler {

    /**
     * TODO {@see JdbcUtils.getResultSetValue}
     * TODO REFACTOR
     * @param fieldType
     * @param rs
     * @param column
     * @return
     */
    public static Object convert(Class<?> fieldType, ResultSet rs, String column) {
        Object value = null;
        try {
            if (fieldType == Integer.class) {
                value = rs.getInt(column);
            } else if (fieldType == Long.class) {
                value = rs.getLong(column);
            } else if (fieldType == Boolean.class) {
                value = rs.getBoolean(column);
            } else if (fieldType == String.class) {
                value = rs.getString(column);
            } else if (fieldType == Float.class) {
                value = rs.getFloat(column);
            } else if (fieldType == Double.class) {
                value = rs.getDouble(column);
            } else if (fieldType == BigDecimal.class) {
                value = rs.getBigDecimal(column);
            } else if (fieldType == BigInteger.class) {
                value = rs.getBigDecimal(column).toBigInteger();
            } else if (fieldType == Date.class || fieldType == java.util.Date.class) {
                value = Optional.ofNullable(rs.getTimestamp(column))
                        .map(e -> new Date(e.getTime()))
                        .orElse(null);
            } else if (fieldType == Timestamp.class) {
                value = rs.getTimestamp(column);
            } else if (fieldType == Time.class) {
                value = rs.getTime(column);
            } else {
                value = rs.getObject(column);
            }
            return value;
        } catch (Exception e) {
            throw new TyrionRuntimException("Type value convert failed", e);
        }
    }
}
