package de.jungblut.graph.mst;

import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

public class PrimTest extends TestCase {

  @Test
  public void testCalculateMST() {

    Graph<Integer, String, Integer> g = TestGraphProvider
        .getWikipediaExampleGraph();
    Prim<Integer, String> instance = Prim.getInstance();
    Graph<Integer, String, Integer> mst = instance
        .constructMinimumSpanningTree(g, g.getVertex(0).getVertexId());

    Set<Vertex<Integer, String>> vertexSet = mst.getVertexSet();
    // all vertices must be contained
    assertEquals(g.getNumVertices(), vertexSet.size());
    // in the wikipedia graph we can only have 9 edges to calculate the MST
    assertEquals(9, mst.getNumEdges());

    // sum the edge weights to get the cost cost
    int cost = 0;
    for (Vertex<Integer, String> v : vertexSet) {
      Set<Edge<Integer, Integer>> edges = mst.getEdges(v.getVertexId());
      for (Edge<Integer, Integer> e : edges) {
        cost += e.getValue();
      }
    }

    assertEquals(1278, cost);

  }
}
