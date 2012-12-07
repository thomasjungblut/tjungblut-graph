package de.jungblut.graph;

import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.model.VertexImpl;

public class GraphWalkerTest extends TestCase {

  @Test
  public void testBreadthFirstTraversal() throws Exception {

    Graph<Integer, String, Integer> g = TestGraphProvider
        .getWikipediaExampleGraph();
    Iterator<VertexImpl<Integer, String>> it = GraphWalker.breadthFirst(g, g
        .getVertex(0).getVertexId());
    // this actually might have order involved, which is not guranteed by the
    // walker
    int[] iterationIds = new int[] { 0, 4, 1, 2, 9, 5, 7, 6, 8, 3 };
    int index = 0;
    while (it.hasNext()) {
      VertexImpl<Integer, String> next = it.next();
      assertEquals(iterationIds[index++], next.getVertexId().intValue());
    }
  }

  @Test
  public void testdepthFirstTraversal() throws Exception {

    Graph<Integer, String, Integer> g = TestGraphProvider
        .getWikipediaExampleGraph();
    Iterator<VertexImpl<Integer, String>> it = GraphWalker.depthFirst(g, g
        .getVertex(0).getVertexId());
    // this actually might have order involved, which is not guranteed by the
    // walker
    int[] iterationIds = new int[] { 0, 2, 6, 7, 9, 8, 5, 3, 1, 4 };
    int index = 0;
    while (it.hasNext()) {
      VertexImpl<Integer, String> next = it.next();
      assertEquals(iterationIds[index++], next.getVertexId().intValue());
    }
  }
}
