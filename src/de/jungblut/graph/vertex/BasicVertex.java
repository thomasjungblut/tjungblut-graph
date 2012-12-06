package de.jungblut.graph.vertex;

/**
 * Basic vertex implementation that is a simple interface implementation. The
 * noteworthy functionality in this class is the equals and hashcode
 * implementation, which is solely focussed on the vertex id. So every composed
 * vertex will be equal to another vertex if their ids match.
 * 
 * @author thomas.jungblut
 * 
 */
public class BasicVertex implements Vertex {

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
    return hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return equals(this, (Vertex) obj);
  }

  @Override
  public String toString() {
    return "BasicVertex [vertexId=" + vertexId + "]";
  }

  /**
   * Hashcode by the vertex id.
   */
  public static int hashCode(Vertex v) {
    final int prime = 31;
    int result = 1;
    result = prime * result + v.getVertexId();
    return result;
  }

  /**
   * Equals between two vertices based on their id.
   */
  public static boolean equals(Vertex v, Vertex v2) {
    if (v == v2)
      return true;
    return v.getVertexId() == v2.getVertexId();
  }

}
