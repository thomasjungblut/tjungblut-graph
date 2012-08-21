package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

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
   * the "Dijkstra's algorithm".
   * 
   * @return a container that includes the path and the costs.
   */
  public static WeightedEdgeContainer<CostVertex> findShortestPaths(
      Graph<CostVertex> graph, CostVertex start) {
    // some datastructure needed
    PriorityQueue<DijkstraPair> distance = new PriorityQueue<>();
    HashMap<CostVertex, Integer> pathDistance = new HashMap<>();
    HashMap<CostVertex, CostVertex> ancestors = new HashMap<>();
    HashSet<CostVertex> vertices = new HashSet<>();

    // initialize them
    initialize(graph, start, distance, ancestors, vertices);

    // main algorithm
    while (!vertices.isEmpty()) {
      DijkstraPair u = distance.poll();
      vertices.remove(u.vertex);
      for (CostVertex v : graph.getAdjacentVertices(u.vertex.getVertexId())) {
        if (vertices.contains(v)) {
          updateDistance(graph, distance, u, v, pathDistance, ancestors);
        }
      }
    }
    return new WeightedEdgeContainer<CostVertex>(pathDistance, ancestors);
  }

  /**
   * Inits the datastructures. Sets all vertices other than the start to
   * infinity distance (Integer.MAX_VALUE), adds the seed distance of start to
   * zero and adds a null ancestor for the start. Also this fills the vertices
   * set by adding all known vertices in this graph.
   */
  private static void initialize(Graph<CostVertex> g, CostVertex start,
      PriorityQueue<DijkstraPair> distance,
      Map<CostVertex, CostVertex> ancestors, HashSet<CostVertex> vertices) {
    // initialize the matrix with infinity = max_value
    for (CostVertex v : g.getVertexSet()) {
      if (v.getVertexId() != start.getVertexId()) {
        distance.add(new DijkstraPair(v, Integer.MAX_VALUE));
      }
    }
    vertices.addAll(g.getVertexSet());
    // set the distance from start to start to zero
    distance.add(new DijkstraPair(start, 0));
    // set the ancestors for start to null, cause it never gets them
    ancestors.put(start, null);
  }

  /**
   * Updates the distance if it needs to be done (if the new found distance is
   * shorter, or we haven't found a path yet). It alters the distance queue by
   * removing the old entry for a vertex and adding it with the new distance if
   * the update needs to be done. Also an ancestor is getting created for the
   * given vertex.
   * 
   * @param u the origin vertex/distance pair
   * @param v the target vertex
   */
  private static void updateDistance(Graph<CostVertex> g,
      PriorityQueue<DijkstraPair> distance, DijkstraPair u, CostVertex v,
      HashMap<CostVertex, Integer> path,
      HashMap<CostVertex, CostVertex> ancestors) {
    int summedLength = u.distance + v.getCost();
    // usually we have no length associated because we haven't visited this
    // vertex v, in this case currentLength is null
    Integer currentLength = path.get(v);
    if (currentLength == null || summedLength < currentLength) {
      path.put(v, summedLength);
      // seek for the item v in the prio queue and remove
      Iterator<DijkstraPair> iterator = distance.iterator();
      while (iterator.hasNext()) {
        if (iterator.next().vertex.getVertexId() == v.getVertexId()) {
          iterator.remove();
          break;
        }
      }
      // add it again to the heap to be priotized correctly
      distance.add(new DijkstraPair(v, summedLength));
      ancestors.put(v, u.vertex);
    }
  }

  /**
   * Just a simple pair for the priority queue.
   */
  static class DijkstraPair implements Comparable<DijkstraPair> {

    CostVertex vertex;
    int distance;

    public DijkstraPair(CostVertex vertex, int distance) {
      super();
      this.vertex = vertex;
      this.distance = distance;
    }

    @Override
    public int compareTo(DijkstraPair o) {
      return Integer.compare(distance, o.distance);
    }

    @Override
    public String toString() {
      return distance + "";
    }

  }

}
