package de.jungblut.graph.search;

import java.util.List;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;

public class AStarTest extends TestCase {

  @Test
  public void testAStarSearch() throws Exception {
    Graph<Integer, String, Integer> g = TestGraphProvider
        .getWikipediaExampleGraph();
    AStar<Integer, String> instance = AStar.newInstance();
    WeightedEdgeContainer<Integer> container = instance.startAStarSearch(g, g
        .getVertex(0).getVertexId(), g.getVertex(9).getVertexId());

    int[] costs = new int[] { 0, 85, 217, 503, 173, 165, 403, 320, 415, 487 };
    for (Entry<Integer, Integer> entry : container.getPathCosts().entrySet()) {
      assertEquals(costs[entry.getKey()], entry.getValue().intValue());
    }

    int[] pathIds = new int[] { 7, 2, 0 };
    int index = 0;
    List<Integer> path = container
        .reconstructPath(g.getVertex(9).getVertexId());
    for (Integer entry : path) {
      assertEquals(pathIds[index++], entry.intValue());
    }
  }

}
