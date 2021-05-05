package com.weaponlin.inf.tyrion.executor.result;

import java.sql.ResultSet;

public interface ResultMapHandler<T> {
    T mapRow(ResultSet rs);
}
