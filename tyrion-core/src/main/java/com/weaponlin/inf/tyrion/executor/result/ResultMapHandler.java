package com.weaponlin.inf.tyrion.executor.result;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public interface ResultMapHandler<T> extends RowMapper<T> {
    T mapRow(ResultSet rs);
}
