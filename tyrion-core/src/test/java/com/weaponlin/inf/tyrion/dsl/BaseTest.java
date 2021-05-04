package com.weaponlin.inf.tyrion.dsl;

import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected void equals(List<Object> parameters, Object[] objects) {
        assertEquals(parameters.size(), objects.length);
        for (int i = 0; i < objects.length; i++) {
            assertEquals(parameters.get(i), objects[i]);
        }
    }

    protected void assertEquals2(List<Object> expectedParameters, List<Object> actualParameters) {
        assertEquals(expectedParameters.size(), actualParameters.size());
        for (int i = 0; i < actualParameters.size(); i++) {
            assertEquals(expectedParameters.get(i), actualParameters.get(i));
        }
    }

    protected List<Object> getColumns(SQLParameter sqlParameter) {
        assertNotNull(sqlParameter);
        List<SelectBuilder.RowMap> rowMaps = sqlParameter.getRowMaps();
        return rowMaps.stream()
                .map(SelectBuilder.RowMap::getColumn)
                .collect(toList());
    }
}
