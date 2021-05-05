package com.weaponlin.inf.tyrion.executor;

import com.weaponlin.inf.tyrion.dsl.builder.DeleteBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.InsertBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.UpdateBuilder;

import java.util.List;

public class BuilderExecutor {

    private final Executor executor;

    public BuilderExecutor(Executor executor) {
        this.executor = executor;
    }

    public <R, T> SelectBuilder<R, T> select() {
        return new SelectBuilder<>(executor);
    }

    public InsertBuilder insert() {
        return new InsertBuilder(executor);
    }

    public <T> int insert(T t) {
        return executor.insert(t);
    }

    public <T> int batchInsert(List<T> list) {
        return executor.insert(list);
    }

    public UpdateBuilder update() {
        return new UpdateBuilder(executor);
    }

    public DeleteBuilder delete() {
        return new DeleteBuilder(executor);
    }
}
