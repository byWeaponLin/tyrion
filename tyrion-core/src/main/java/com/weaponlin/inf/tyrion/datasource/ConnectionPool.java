package com.weaponlin.inf.tyrion.datasource;


import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Connection;
import java.util.LinkedList;

@Getter
public class ConnectionPool {

    /**
     * 空闲连接
     */
    private LinkedList<PooledConnection> idleConnections;
    /**
     * 繁忙连接
     */
    private LinkedList<PooledConnection> activeConnections;

    public ConnectionPool() {
        this.idleConnections = new LinkedList<>();
        this.activeConnections = new LinkedList<>();
    }

    public void addPool(PooledConnection pooledConnection) {
        if (pooledConnection != null) {
            idleConnections.addLast(pooledConnection);
        }
    }

    public void recycle(PooledConnection pooledConnection) {
        activeConnections.remove(pooledConnection);
        idleConnections.add(pooledConnection);
    }

    public Connection getConnection() {
        synchronized (ConnectionPool.class) {
            if (CollectionUtils.isNotEmpty(idleConnections)) {
                PooledConnection pooledConnection = idleConnections.removeFirst();
                activeConnections.addLast(pooledConnection);
                return pooledConnection.getProxyConnection();
            } else {
                // TODO create new connection?
                throw new TyrionRuntimException("no idle connection right now, please retry");
            }
        }
    }
}
