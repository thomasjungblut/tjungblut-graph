package de.jungblut.graph.vertex;

public final class NamedVertex implements Vertex {

  private final Vertex vertex;
  private final String name;

  public NamedVertex(int id, String name) {
    this.vertex = new BasicVertex(id);
    this.name = name;
  }

  public NamedVertex(Vertex vertex, String name) {
    this.vertex = vertex;
    this.name = name;
  }

  @Override
  public int getVertexId() {
    return vertex.getVertexId();
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
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
    if (vertex == null) {
      if (other.vertex != null)
        return false;
    } else if (!vertex.equals(other.vertex))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Vertex [name=" + getName() + "]";
  }

}
