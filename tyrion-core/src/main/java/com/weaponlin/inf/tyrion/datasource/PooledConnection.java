package com.weaponlin.inf.tyrion.datasource;

import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class PooledConnection implements InvocationHandler {

    private static final Class<?>[] IFACES = new Class<?>[]{Connection.class};

    private Connection connection;

    private ConnectionPool connectionPool;

    @Getter
    private Connection proxyConnection;

    private Integer hashCode;

    public PooledConnection(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
        this.hashCode = connection.hashCode();
        this.proxyConnection = (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("close".equals(method.getName())) {
            // TODO may be error
            connectionPool.recycle(this);
            return null;
        } else {
            return method.invoke(connection, args);
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection) {
            return connection.hashCode() == ((PooledConnection) obj).connection.hashCode();
        } else if (obj instanceof Connection) {
            return hashCode == obj.hashCode();
        } else {
            return false;
        }
    }
}
