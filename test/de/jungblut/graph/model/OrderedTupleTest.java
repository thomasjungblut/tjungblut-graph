package de.jungblut.graph.model;

import org.junit.Assert;
import org.junit.Test;

public class OrderedTupleTest {

    @Test
    public void testTupleOrdering() {
        OrderedTuple<String> t = new OrderedTuple<>("a", "b");
        Assert.assertEquals("b", t.getFirst());
        Assert.assertEquals("a", t.getSecond());
    }

    @Test
    public void testTupleOrderingEqual() {
        OrderedTuple<String> t = new OrderedTuple<>("a", "a");
        Assert.assertEquals("a", t.getFirst());
        Assert.assertEquals("a", t.getSecond());
    }
}
