package de.jungblut.graph.vertex;

/**
 * Cost vertex. This is a wrapper to a vertex and adds a cost.
 * 
 * @author thomas.jungblut
 * 
 */
public final class CostVertex implements Vertex {

  private final Vertex vertex;
  private final int cost;

  public CostVertex(int id, int cost) {
    this.vertex = new BasicVertex(id);
    this.cost = cost;
  }

  public CostVertex(Vertex vertex, int cost) {
    this.vertex = vertex;
    this.cost = cost;
  }

  @Override
  public Vertex getVertex() {
    return vertex;
  }

  @Override
  public int getVertexId() {
    return vertex.getVertexId();
  }

  public int getCost() {
    return cost;
  }

  @Override
  public int hashCode() {
    return BasicVertex.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return BasicVertex.equals(this, (Vertex) obj);
  }

  @Override
  public String toString() {
    return "CostVertex [vertex=" + vertex + ", cost=" + cost + "]";
  }

}
