package de.jungblut.graph.edge;

import de.jungblut.graph.vertex.Vertex;

public final class BasicEdge implements Edge {

  private final Vertex fromVertex;
  private final Vertex toVertex;

  public BasicEdge(Vertex fromVertex, Vertex toVertex) {
    super();
    this.fromVertex = fromVertex;
    this.toVertex = toVertex;
  }

  @Override
  public Vertex getFromVertex() {
    return fromVertex;
  }

  @Override
  public Vertex getToVertex() {
    return toVertex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((fromVertex == null) ? 0 : fromVertex.hashCode());
    result = prime * result + ((toVertex == null) ? 0 : toVertex.hashCode());
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
    BasicEdge other = (BasicEdge) obj;
    if (fromVertex == null) {
      if (other.fromVertex != null)
        return false;
    } else if (!fromVertex.equals(other.fromVertex))
      return false;
    if (toVertex == null) {
      if (other.toVertex != null)
        return false;
    } else if (!toVertex.equals(other.toVertex))
      return false;
    return true;
  }

}
