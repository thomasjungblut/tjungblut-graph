package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.Vertex;

/**
 * Container class for the shortest path and its costs.
 * 
 * @author thomas.jungblut
 */
public final class WeightedEdgeContainer<V extends Vertex> {

  private final HashMap<V, Integer> path;
  private final HashMap<V, V> ancestors;

  public WeightedEdgeContainer(HashMap<V, Integer> path, HashMap<V, V> ancestors) {
    super();
    this.path = path;
    this.ancestors = ancestors;
  }

  public HashMap<V, V> getAncestors() {
    return ancestors;
  }

  public HashMap<V, Integer> getPath() {
    return path;
  }

  /**
   * Reconstructs the found path.
   * 
   * @param g the graph to use.
   * @param end the end vertex to reconstruct the path from
   * @return the path with the visited vertices.
   */
  public List<V> reconstructPath(Graph<V> g, V end) {
    List<V> list = Lists.newArrayList();
    V predecessor = end;
    while ((predecessor = ancestors.get(predecessor)) != null) {
      list.add(predecessor);
    }

    return list;
  }
}
