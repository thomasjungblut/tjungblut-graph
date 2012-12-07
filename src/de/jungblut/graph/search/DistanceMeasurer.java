package de.jungblut.graph.search;

import de.jungblut.graph.Graph;

/**
 * Distance measurement interface for graphs.
 * 
 * @author thomas.jungblut
 */
public interface DistanceMeasurer<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

  /**
   * Measure the distance beetween the start and end vertex in the graph g.
   * 
   * @return the distance between these nodes.
   */
  public double measureDistance(Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g,
      VERTEX_ID start, VERTEX_ID goal);

}
