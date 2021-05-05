package com.weaponlin.inf.tyrion.datasource;

import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class PooledDatasourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void get_connection() throws SQLException {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin"
        );

        assertEquals(8, pooledDatasource.getPoolSize().intValue());
        assertEquals(8, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(0, pooledDatasource.getConnectionPool().getActiveConnections().size());
        Connection connection = pooledDatasource.getConnection();
        assertNotNull(connection);
        assertEquals(7, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(1, pooledDatasource.getConnectionPool().getActiveConnections().size());

        connection.close();
        assertEquals(8, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(0, pooledDatasource.getConnectionPool().getActiveConnections().size());
    }

    @Test
    public void throw_exception_when_no_idle_connection() {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin",
                3
        );
        assertEquals(3, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(0, pooledDatasource.getConnectionPool().getActiveConnections().size());

        pooledDatasource.getConnection();
        assertEquals(2, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(1, pooledDatasource.getConnectionPool().getActiveConnections().size());

        pooledDatasource.getConnection();
        assertEquals(1, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(2, pooledDatasource.getConnectionPool().getActiveConnections().size());

        pooledDatasource.getConnection();
        assertEquals(0, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(3, pooledDatasource.getConnectionPool().getActiveConnections().size());


        thrown.expect(TyrionRuntimException.class);
        thrown.expectMessage("no idle connection right now, please retry");
        pooledDatasource.getConnection();
    }

    @Test
    public void with_max_pool_size_if_exceed() {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin",
                300
        );
        assertEquals(50, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(0, pooledDatasource.getConnectionPool().getActiveConnections().size());
    }

    @Test
    public void with_min_pool_size_if_exceed() {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin",
                2
        );
        assertEquals(3, pooledDatasource.getConnectionPool().getIdleConnections().size());
        assertEquals(0, pooledDatasource.getConnectionPool().getActiveConnections().size());
    }
}