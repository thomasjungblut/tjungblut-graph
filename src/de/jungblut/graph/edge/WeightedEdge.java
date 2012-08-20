package de.jungblut.graph.edge;

import de.jungblut.graph.vertex.Vertex;

public final class WeightedEdge implements Edge {

  private final Edge edge;
  private final int weight;

  public WeightedEdge(Edge edge, int weight) {
    super();
    this.edge = edge;
    this.weight = weight;
  }

  @Override
  public Vertex getFromVertex() {
    return edge.getFromVertex();
  }

  @Override
  public Vertex getToVertex() {
    return edge.getToVertex();
  }

  public int getWeight() {
    return weight;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((edge == null) ? 0 : edge.hashCode());
    result = prime * result + weight;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WeightedEdge other = (WeightedEdge) obj;
    if (edge == null) {
      if (other.edge != null)
        return false;
    } else if (!edge.equals(other.edge))
      return false;
    if (weight != other.weight)
      return false;
    return true;
  }

}
