package de.jungblut.graph.model;

/**
 * A tuple for id and cost that is comparable to the ascending distance.
 * 
 * @author thomas.jungblut
 */
public class IdCostTuple<VERTEX_ID> implements
    Comparable<IdCostTuple<VERTEX_ID>> {

  private final VERTEX_ID vertexId;
  private final int distance;
  private VERTEX_ID source;

  public IdCostTuple(VERTEX_ID vertex, int distance) {
    this.vertexId = vertex;
    this.distance = distance;
  }

  public IdCostTuple(VERTEX_ID vertex, VERTEX_ID source, int distance) {
    super();
    this.vertexId = vertex;
    this.source = source;
    this.distance = distance;
  }

  @Override
  public int compareTo(IdCostTuple<VERTEX_ID> o) {
    return Integer.compare(distance, o.distance);
  }

  @Override
  public String toString() {
    return distance + "";
  }

  public int getDistance() {
    return this.distance;
  }

  public VERTEX_ID getVertexId() {
    return this.vertexId;
  }

  public VERTEX_ID getSource() {
    return this.source;
  }

}
