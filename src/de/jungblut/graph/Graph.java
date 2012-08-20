package de.jungblut.graph;

import java.util.List;
import java.util.Set;

import de.jungblut.graph.vertex.Vertex;

/**
 * Basic graph interface, for example concrete subclasses can be sparse
 * adjacency lists, dense matrices or rdf storages as well as graph databases.
 * 
 * @author thomas.jungblut
 * 
 * @param <V> the {@link Vertex} type that should be used in this graph.
 */
public interface Graph<V extends Vertex> {

  /**
   * Adds a vertex with a list of adjacents to it.
   */
  public void addVertex(V vertex, List<V> adjacents);

  /**
   * Adds a vertex with a list of adjacents to it.
   */
  public void addVertex(V vertex, @SuppressWarnings("unchecked") V... adjacents);

  /**
   * Gets a set of adjacent vertices from a given vertex id.
   */
  public Set<V> getAdjacentVertices(int vertexId);

  /**
   * Get the vertex by its id.
   */
  public V getVertex(int vertexId);

  /**
   * Gets a set of adjacent vertices from a given vertex.
   */
  public Set<V> getAdjacentVertices(V vertex);

  /**
   * Gets all vertices associated with this graph.
   */
  public Set<V> getVertexSet();

}
