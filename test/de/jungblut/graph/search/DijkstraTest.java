package de.jungblut.graph.search;

import java.util.List;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.vertex.CostVertex;

public class DijkstraTest extends TestCase {

  @Test
  public void testShortestPaths() throws Exception {

    Graph<CostVertex> g = TestGraphProvider.getWikipediaExampleGraph();

    WeightedEdgeContainer<CostVertex> container = Dijkstra.findShortestPaths(g,
        g.getVertex(0));

    int[] costs = new int[] { 0, 85, 217, 503, 173, 165, 403, 320, 415, 487 };

    for (Entry<CostVertex, Integer> entry : container.getPath().entrySet()) {
      assertEquals(costs[entry.getKey().getVertexId()], entry.getValue()
          .intValue());
    }

    int[] pathIds = new int[] { 7, 2, 0 };
    int index = 0;
    List<CostVertex> path = container.reconstructPath(g, g.getVertex(9));
    for (CostVertex entry : path) {
      assertEquals(pathIds[index++], entry.getVertexId());
    }
  }

}
