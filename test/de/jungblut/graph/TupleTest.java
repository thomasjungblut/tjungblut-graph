package de.jungblut.graph;

import org.junit.Assert;
import org.junit.Test;

public class TupleTest {

  @Test
  public void testTuple() {
    Tuple<Integer, String> tp = new Tuple<>(1, "abc");

    Assert.assertEquals(1, tp.getFirst().intValue());
    Assert.assertEquals("abc", tp.getSecond());
  }
}
