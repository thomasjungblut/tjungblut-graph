package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Container class for the shortest path and its costs.
 * 
 * @author thomas.jungblut
 */
public final class WeightedEdgeContainer<VERTEX_ID> {

  private final HashMap<VERTEX_ID, Integer> pathCosts;
  private final HashMap<VERTEX_ID, VERTEX_ID> ancestors;

  public WeightedEdgeContainer(HashMap<VERTEX_ID, Integer> costs,
      HashMap<VERTEX_ID, VERTEX_ID> ancestors) {
    super();
    this.pathCosts = costs;
    this.ancestors = ancestors;
  }

  /**
   * @return a map that contains VERTEX_ID to VERTEX_ID relation denoting the
   *         predecessor on the right (value) side and the successor on the key
   *         side.
   */
  public HashMap<VERTEX_ID, VERTEX_ID> getAncestors() {
    return ancestors;
  }

  /**
   * @return a map that contains VERTEX_ID to cost mapping. It represents the
   *         cost that needs to be spend to reach a given vertex.
   */
  public HashMap<VERTEX_ID, Integer> getPathCosts() {
    return pathCosts;
  }

  /**
   * Reconstructs the found path.
   * 
   * @param end the end vertex to reconstruct the path from
   * @return the path with the visited vertices.
   */
  public List<VERTEX_ID> reconstructPath(VERTEX_ID end) {
    List<VERTEX_ID> list = Lists.newArrayList();
    VERTEX_ID predecessor = end;
    while ((predecessor = ancestors.get(predecessor)) != null) {
      list.add(predecessor);
    }

    return list;
  }
}
