package de.jungblut.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class TupleTest {

    @Test
    public void testTuple() {
        Tuple<Integer, String> tp = new Tuple<>(1, "abc");

        Assert.assertEquals(1, tp.getFirst().intValue());
        Assert.assertEquals("abc", tp.getSecond());
    }

    @Test
    public void testHashSetRetrieval() {
        HashSet<Tuple<Integer, String>> set = new HashSet<>();
        set.add(new Tuple<>(1, "abc"));

        Assert.assertTrue(set.contains(new Tuple<>(1, "abc")));
        Assert.assertTrue(set.contains(new Tuple<>(1, "whatever")));
        Assert.assertFalse(set.contains(new Tuple<>(0, "whatever")));
    }

    @Test
    public void testCompareTo() {
        Assert.assertEquals(0, new Tuple<>(1, "abc").compareTo(new Tuple<>(1, "whatever")));
        Assert.assertEquals(-1, new Tuple<>(0, "abc").compareTo(new Tuple<>(1, "whatever")));
        Assert.assertEquals(1, new Tuple<>(2, "abc").compareTo(new Tuple<>(1, "whatever")));
    }
}
