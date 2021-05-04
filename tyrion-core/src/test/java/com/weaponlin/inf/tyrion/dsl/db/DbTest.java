package com.weaponlin.inf.tyrion.dsl.db;

import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.sample.entity.User;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class DbTest {

    public static void main(String[] args) {
        SQLParameter sqlParameter = DSL.select().column("id", "name", "gender", "age")
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.name("age").gt(PlaceholderOperand.value(21)))
                .build();
        Connection conn = DbConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sqlParameter.getSql())) {
            setParameters(pstmt, sqlParameter.getParameters());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = (Long) rs.getObject("id");
                int age = rs.getInt("age");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                System.out.println(String.format("%d \t %d \t %s \t %s", id, age, name, gender));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setParameters(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        for (int i = 0; CollectionUtils.isNotEmpty(parameters) && i < parameters.size(); i++) {
            pstmt.setObject(i + 1, parameters.get(i));
        }
    }
}
