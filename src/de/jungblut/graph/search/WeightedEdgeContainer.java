package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Container class for the shortest path and its costs.
 * 
 * @author thomas.jungblut
 */
public final class WeightedEdgeContainer<VERTEX_ID, EDGE_VALUE> {

  private final HashMap<VERTEX_ID, EDGE_VALUE> path;
  private final HashMap<VERTEX_ID, VERTEX_ID> ancestors;

  public WeightedEdgeContainer(HashMap<VERTEX_ID, EDGE_VALUE> path,
      HashMap<VERTEX_ID, VERTEX_ID> ancestors) {
    super();
    this.path = path;
    this.ancestors = ancestors;
  }

  public HashMap<VERTEX_ID, VERTEX_ID> getAncestors() {
    return ancestors;
  }

  public HashMap<VERTEX_ID, EDGE_VALUE> getPath() {
    return path;
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
