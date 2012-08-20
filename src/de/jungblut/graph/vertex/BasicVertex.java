package de.jungblut.graph.vertex;

public final class BasicVertex implements Vertex {

  private final int vertexId;

  public BasicVertex(int vertexId) {
    this.vertexId = vertexId;
  }

  @Override
  public int getVertexId() {
    return vertexId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + vertexId;
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
    NamedVertex other = (NamedVertex) obj;
    if (vertexId != other.getVertexId())
      return false;
    return true;
  }

}
