package de.jungblut.graph.vertex;

/**
 * Named vertex. This is a wrapper to a vertex and adds a name.
 * 
 * @author thomas.jungblut
 * 
 */
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

  @Override
  public Vertex getVertex() {
    return vertex;
  }

  public String getName() {
    return name;
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
    return "NamedVertex [vertex=" + vertex + ", name=" + name + "]";
  }

}
