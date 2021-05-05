package com.weaponlin.inf.tyrion.executor.result;

import java.sql.ResultSet;
import java.util.List;

public interface ResultMapHandler<R> {
    R mapRow(ResultSet rs);

    List<R> mapRows(ResultSet rs);
}
