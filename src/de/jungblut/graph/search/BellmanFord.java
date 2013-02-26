package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import de.jungblut.graph.Graph;
import de.jungblut.graph.GraphWalker;
import de.jungblut.graph.Tuple;
import de.jungblut.graph.model.Edge;

/**
 * Implementation of the Bellman Ford algorithm.
 * 
 * @author thomas.jungblut
 * 
 */
public final class BellmanFord<VERTEX_ID, VERTEX_VALUE> {

  /**
   * "Positive infinity", set to Integer.MAX_VALUE - 100k. In case your solution
   * wraps up and generates negative cycles/distances, you should lower that
   * limit to (Integer.MAX_VALUE - the expected maximum distance) in that graph.
   */
  public static int POSITIVE_INFINITY = Integer.MAX_VALUE - 100000;

  /**
   * Finds the shortest paths from the start vertex.
   * 
   * @return a container that includes the path and the costs. If the algorithm
   *         fails to find a shortest path, because there is a negative cycle it
   *         just returns "null".
   */
  public WeightedEdgeContainer<VERTEX_ID> findShortestPaths(
      Graph<VERTEX_ID, VERTEX_VALUE, Integer> graph, VERTEX_ID start) {
    // some datastructure needed
    HashMap<VERTEX_ID, Integer> pathDistance = new HashMap<>();
    HashMap<VERTEX_ID, VERTEX_ID> ancestors = new HashMap<>();
    Set<VERTEX_ID> vertices = graph.getVertexIDSet();
    // initialize them
    ancestors.put(start, null);
    pathDistance.put(start, 0);
    // main algorithm
    for (int i = 0; i < vertices.size() - 1; i++) {
      // loop over all edges
      Iterator<Tuple<VERTEX_ID, Edge<VERTEX_ID, Integer>>> edges = GraphWalker
          .iterateEdges(graph, vertices);
      while (edges.hasNext()) {
        Tuple<VERTEX_ID, Edge<VERTEX_ID, Integer>> next = edges.next();
        VERTEX_ID u = next.getFirst();
        Edge<VERTEX_ID, Integer> edge = next.getSecond();
        Integer uDist = safeGet(pathDistance, u);
        Integer vDist = safeGet(pathDistance, edge.getDestinationVertexID());
        if ((uDist + edge.getValue()) < vDist) {
          pathDistance.put(edge.getDestinationVertexID(),
              uDist + edge.getValue());
          ancestors.put(u, edge.getDestinationVertexID());
        }

      }
    }
    // check for negative cycles
    Iterator<Tuple<VERTEX_ID, Edge<VERTEX_ID, Integer>>> edges = GraphWalker
        .iterateEdges(graph, vertices);
    while (edges.hasNext()) {
      Tuple<VERTEX_ID, Edge<VERTEX_ID, Integer>> next = edges.next();
      VERTEX_ID u = next.getFirst();
      Edge<VERTEX_ID, Integer> edge = next.getSecond();
      Integer uDist = safeGet(pathDistance, u);
      Integer vDist = safeGet(pathDistance, edge.getDestinationVertexID());
      if ((uDist + edge.getValue()) < vDist) {
        // found a negative cycle
        return null;
      }
    }
    return new WeightedEdgeContainer<>(pathDistance, ancestors);
  }

  /**
   * @return if a distance isn't mapped in pathDistance, then return
   *         POSITIVE_INFINITY. Else the distance that was mapped.
   */
  private Integer safeGet(HashMap<VERTEX_ID, Integer> pathDistance,
      VERTEX_ID destinationVertexID) {
    Integer vDist = pathDistance.get(destinationVertexID);
    if (vDist == null) {
      vDist = POSITIVE_INFINITY;
    }
    return vDist;
  }

  public static final <VERTEX_ID, VERTEX_VALUE> BellmanFord<VERTEX_ID, VERTEX_VALUE> newInstance() {
    return new BellmanFord<>();
  }
}
