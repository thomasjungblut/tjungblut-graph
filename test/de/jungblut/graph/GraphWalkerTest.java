package de.jungblut.graph;

import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.vertex.CostVertex;

public class GraphWalkerTest extends TestCase {

  @Test
  public void testBreadthFirstTraversal() throws Exception {

    Graph<CostVertex> g = TestGraphProvider.getWikipediaExampleGraph();
    Iterator<CostVertex> it = GraphWalker.breadthFirst(g, g.getVertex(0));
    // this actually might have order involved, which is not guranteed by the
    // walker
    int[] iterationIds = new int[] { 0, 4, 1, 2, 9, 5, 7, 6, 8, 3 };
    int index = 0;
    while (it.hasNext()) {
      CostVertex next = it.next();
      assertEquals(iterationIds[index++], next.getVertexId());
    }
  }

  @Test
  public void testdepthFirstTraversal() throws Exception {

    Graph<CostVertex> g = TestGraphProvider.getWikipediaExampleGraph();
    Iterator<CostVertex> it = GraphWalker.depthFirst(g, g.getVertex(0));
    // this actually might have order involved, which is not guranteed by the
    // walker
    int[] iterationIds = new int[] { 0, 2, 6, 7, 9, 8, 5, 3, 1, 4 };
    int index = 0;
    while (it.hasNext()) {
      CostVertex next = it.next();
      assertEquals(iterationIds[index++], next.getVertexId());
    }
  }
}
