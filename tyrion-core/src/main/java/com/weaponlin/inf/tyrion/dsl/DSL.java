package com.weaponlin.inf.tyrion.dsl;


import com.weaponlin.inf.tyrion.dsl.builder.DeleteBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.InsertBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.UpdateBuilder;

/**
 * DSL entrance, open the gate of new sql generator.
 */
public class DSL {

    public static <R, T> SelectBuilder<R, T> select() {
        return new SelectBuilder<>();
    }

    public static InsertBuilder insert() {
        return new InsertBuilder();
    }


    public static UpdateBuilder update() {
        return new UpdateBuilder();
    }

    public static DeleteBuilder delete() {
        return new DeleteBuilder();
    }
}
