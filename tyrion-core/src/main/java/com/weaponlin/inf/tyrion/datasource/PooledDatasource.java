package com.weaponlin.inf.tyrion.datasource;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Slf4j
@Setter
@Getter
public class PooledDatasource implements DataSource {

    public static final int MAX_POOL = 50;
    public static final int MIN_POOL = 3;

    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    private Integer poolSize = 8;

    private ConnectionPool connectionPool;

    public PooledDatasource(@NonNull String driver, @NonNull String url,
                            @NonNull String username, @NonNull String password) {
        this(driver, url, username, password, null);
    }

    public PooledDatasource(@NonNull String driver, @NonNull String url,
                            @NonNull String username, @NonNull String password,
                            Integer poolSize) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionPool = new ConnectionPool();
        if (poolSize != null) {
            if (poolSize < MIN_POOL) {
                this.poolSize = MIN_POOL;
            } else if (poolSize > MAX_POOL) {
                this.poolSize = MAX_POOL;
            } else {
                this.poolSize = poolSize;
            }
        }
        // TODO init connection pool
        initConnectionPool();
    }

    public void initConnectionPool() {
        synchronized (ConnectionPool.class) {
            for (int i = 0; i < poolSize; i++) {
                try {
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PooledConnection pooledConnection = new PooledConnection(connection, connectionPool);
                    connectionPool.addPool(pooledConnection);
                } catch (Exception e) {
                    log.error("init connection pool failed", e);
                }
            }
        }
    }

    @Override
    public Connection getConnection() {
        return connectionPool.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) {
        // TODO set username and password
        return connectionPool.getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
