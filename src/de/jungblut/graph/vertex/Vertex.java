package de.jungblut.graph.vertex;

/**
 * Vertex interface, should just be the identifier of a vertex. The whole
 * library focusses on the composition of vertices with functionality. So you
 * can compose a vertex by wrapping a vertex with additional functionality. This
 * is fine, because it keeps your vertices clean and you don't get huge single
 * monumental classes which seem to have all functionality involved.
 * 
 * @author thomas.jungblut
 * 
 */
public interface Vertex {

  /**
   * @return the id of this vertex.
   */
  public int getVertexId();

  /**
   * @return the wrapped vertex, if none exists, then this is returned.
   */
  public Vertex getVertex();

}
