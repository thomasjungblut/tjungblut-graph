package de.jungblut.graph.search;

import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.Vertex;

/**
 * Distance measurement interface for graphs.
 * 
 * @author thomas.jungblut
 */
public interface DistanceMeasurer<V extends Vertex> {

  /**
   * Measure the distance beetween the start and end vertex in the graph g.
   * 
   * @return the distance between these nodes.
   */
  public double measureDistance(Graph<V> g, V start, V goal);

}
