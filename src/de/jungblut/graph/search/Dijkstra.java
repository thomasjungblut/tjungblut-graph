package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.CostVertex;

/**
 * Basic Dijkstra's algorithm in a graph.
 * 
 * @author thomas.jungblut
 * 
 */
public final class Dijkstra {

  /**
   * Finds the shortest paths from the start vertex in the given path by doing
   * the "dijkstra's algorithm".
   * 
   * @return a container that includes the path and the costs.
   */
  public static WeightedEdgeContainer<CostVertex> findShortestPaths(
      Graph<CostVertex> graph, CostVertex start) {
    // some datastructure needed
    HashMap<CostVertex, Integer> path = new HashMap<>();
    HashMap<CostVertex, CostVertex> ancestors = new HashMap<>();
    HashSet<CostVertex> vertices = new HashSet<>();
    initialize(graph, start, path, ancestors, vertices);
    // main algorithm
    while (!vertices.isEmpty()) {
      CostVertex u = findLowestWeight(vertices, path);
      vertices.remove(u);
      for (CostVertex v : graph.getAdjacentVertices(u.getVertexId())) {
        if (vertices.contains(v)) {
          updateDistance(graph, u, v, v.getCost(), path, ancestors);
        }
      }
    }
    return new WeightedEdgeContainer<CostVertex>(path, ancestors);
  }

  /**
   * Inits the datastructures.
   */
  private static void initialize(Graph<CostVertex> g, CostVertex start,
      Map<CostVertex, Integer> pathMap, Map<CostVertex, CostVertex> ancestors,
      HashSet<CostVertex> vertices) {
    // initialize the matrix with infinity = max_value
    for (CostVertex v : g.getVertexSet()) {
      pathMap.put(v, Integer.valueOf(Integer.MAX_VALUE));
      ancestors.put(v, v);
      vertices.add(v);
    }
    // set the distance from start to start to zero
    pathMap.put(start, Integer.valueOf(0));
    // set the ancestors for start to null, cause it never gets them
    ancestors.put(start, null);
  }

  private static void updateDistance(Graph<CostVertex> g, CostVertex u,
      CostVertex v, Integer weight, HashMap<CostVertex, Integer> path,
      HashMap<CostVertex, CostVertex> ancestors) {
    int summedWeight = path.get(u) + weight;
    if (summedWeight < path.get(v)) {
      path.put(v, summedWeight);
      ancestors.put(v, u);
    }
  }

  // TODO to make it faster we can use a PriorityQueue.
  private static CostVertex findLowestWeight(HashSet<CostVertex> q,
      HashMap<CostVertex, Integer> path) {
    CostVertex lowest = null;
    int currentLowest = -1;
    for (CostVertex v : q) {
      if (lowest == null) {
        lowest = v;
        currentLowest = path.get(lowest);
      } else {
        Integer u = path.get(v);
        if (u.intValue() < currentLowest) {
          lowest = v;
          currentLowest = u;
        }
      }
    }
    return lowest;
  }

}
