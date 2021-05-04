package com.weaponlin.inf.tyrion.sharding;

public interface ShardingStrategy {
    String sharding(Class clazz, Long hashId);
}
