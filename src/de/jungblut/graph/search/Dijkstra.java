package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.IdCostTuple;
import de.jungblut.graph.model.Vertex;

/**
 * Dijkstra's algorithm in a graph implemented with a {@link PriorityQueue}.
 * 
 * @author thomas.jungblut
 */
public final class Dijkstra<VERTEX_ID, VERTEX_VALUE> {

  /**
   * Finds the shortest paths from the start vertex in the given path by doing
   * the "Dijkstra's algorithm".
   * 
   * @return a container that includes the path and the costs.
   */
  public WeightedEdgeContainer<VERTEX_ID, Integer> findShortestPaths(
      Graph<VERTEX_ID, VERTEX_VALUE, Integer> graph, VERTEX_ID start) {
    // some datastructure needed
    PriorityQueue<IdCostTuple<VERTEX_ID>> distance = new PriorityQueue<>();
    HashMap<VERTEX_ID, Integer> pathDistance = new HashMap<>();
    HashMap<VERTEX_ID, VERTEX_ID> ancestors = new HashMap<>();
    HashSet<VERTEX_ID> vertices = new HashSet<>();

    // initialize them
    initialize(graph, start, distance, ancestors, vertices);

    // main algorithm
    while (!vertices.isEmpty()) {
      IdCostTuple<VERTEX_ID> u = distance.poll();
      vertices.remove(u.getVertexId());
      for (Vertex<VERTEX_ID, VERTEX_VALUE> v : graph.getAdjacentVertices(u
          .getVertexId())) {
        if (vertices.contains(v.getVertexId())) {
          updateDistance(graph.getEdge(u.getVertexId(), v.getVertexId()),
              distance, u, v, pathDistance, ancestors);
        }
      }
    }
    return new WeightedEdgeContainer<VERTEX_ID, Integer>(pathDistance,
        ancestors);
  }

  /**
   * Inits the datastructures. Sets all vertices other than the start to
   * infinity distance (Integer.MAX_VALUE), adds the seed distance of start to
   * zero and adds a null ancestor for the start. Also this fills the vertices
   * set by adding all known vertices in this graph.
   */
  private void initialize(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g,
      VERTEX_ID start, PriorityQueue<IdCostTuple<VERTEX_ID>> distance,
      HashMap<VERTEX_ID, VERTEX_ID> ancestors, HashSet<VERTEX_ID> vertices) {
    // initialize the matrix with infinity = max_value
    for (Vertex<VERTEX_ID, VERTEX_VALUE> v : g.getVertexSet()) {
      if (!v.getVertexId().equals(start)) {
        distance.add(new IdCostTuple<VERTEX_ID>(v.getVertexId(),
            Integer.MAX_VALUE));
      }
    }
    vertices.addAll(g.getVertexIDSet());
    // set the distance from start to start to zero
    distance.add(new IdCostTuple<VERTEX_ID>(start, 0));
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
   * @param u the origin vertex
   * @param v the target vertex
   */
  private void updateDistance(Edge<VERTEX_ID, Integer> edge,
      PriorityQueue<IdCostTuple<VERTEX_ID>> distance, IdCostTuple<VERTEX_ID> u,
      Vertex<VERTEX_ID, VERTEX_VALUE> v, HashMap<VERTEX_ID, Integer> path,
      HashMap<VERTEX_ID, VERTEX_ID> ancestors) {
    int summedLength = u.getDistance() + edge.getValue();
    // usually we have no length associated because we haven't visited this
    // vertex v, in this case currentLength is null
    Integer currentLength = path.get(v.getVertexId());
    if (currentLength == null || summedLength < currentLength) {
      path.put(v.getVertexId(), summedLength);
      // seek for the item v in the prio queue and remove
      Iterator<IdCostTuple<VERTEX_ID>> iterator = distance.iterator();
      while (iterator.hasNext()) {
        if (iterator.next().getVertexId().equals(v.getVertexId())) {
          iterator.remove();
          break;
        }
      }
      // add it again to the heap to be priotized correctly
      distance.add(new IdCostTuple<VERTEX_ID>(v.getVertexId(), summedLength));
      ancestors.put(v.getVertexId(), u.getVertexId());
    }

  }

  public static final <VERTEX_ID, VERTEX_VALUE> Dijkstra<VERTEX_ID, VERTEX_VALUE> getInstance() {
    return new Dijkstra<VERTEX_ID, VERTEX_VALUE>();
  }

}
