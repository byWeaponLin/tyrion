package com.weaponlin.inf.tyrion.enums;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LikeOptionTest extends BaseTest {

    @Test
    public void test_none_success() {
        assertEquals("1", LikeOption.NONE.format(1));
        assertEquals("NULL", LikeOption.NONE.format(null));
        assertEquals("what", LikeOption.NONE.format("what"));
        assertEquals("2.2", LikeOption.NONE.format(2.2));
        assertEquals("a", LikeOption.NONE.format('a'));
    }

    @Test
    public void test_left_success() {
        assertEquals("%1", LikeOption.LEFT.format(1));
        assertEquals("%NULL", LikeOption.LEFT.format(null));
        assertEquals("%what", LikeOption.LEFT.format("what"));
        assertEquals("%2.2", LikeOption.LEFT.format(2.2));
        assertEquals("%a", LikeOption.LEFT.format('a'));
    }

    @Test
    public void test_right_success() {
        assertEquals("1%", LikeOption.RIGHT.format(1));
        assertEquals("NULL%", LikeOption.RIGHT.format(null));
        assertEquals("what%", LikeOption.RIGHT.format("what"));
        assertEquals("2.2%", LikeOption.RIGHT.format(2.2));
        assertEquals("a%", LikeOption.RIGHT.format('a'));
    }

    @Test
    public void test_all_success() {
        assertEquals("%1%", LikeOption.ALL.format(1));
        assertEquals("%NULL%", LikeOption.ALL.format(null));
        assertEquals("%what%", LikeOption.ALL.format("what"));
        assertEquals("%2.2%", LikeOption.ALL.format(2.2));
        assertEquals("%a%", LikeOption.ALL.format('a'));
    }
}