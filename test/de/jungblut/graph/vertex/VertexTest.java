package de.jungblut.graph.vertex;

import java.util.HashSet;

import junit.framework.TestCase;

import org.junit.Test;

// basically just checks whether the equals and hashcodes are implemented by their id.
public class VertexTest extends TestCase {

  @Test
  public void testHashCodeEquals() throws Exception {

    HashSet<Vertex> vertexSet = new HashSet<>();
    BasicVertex v = new BasicVertex(1);
    CostVertex vx = new CostVertex(1, 2);
    NamedVertex vn = new NamedVertex(1, "test");

    assertTrue(vertexSet.add(v));
    assertFalse(vertexSet.add(vx));
    assertFalse(vertexSet.add(vn));

    assertTrue(vertexSet.iterator().next() == v);

  }

}
