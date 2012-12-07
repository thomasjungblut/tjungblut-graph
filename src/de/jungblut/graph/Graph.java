package de.jungblut.graph;

import java.util.List;
import java.util.Set;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

/**
 * Basic graph interface, for example concrete subclasses can be sparse
 * adjacency lists, dense matrices or rdf storages as well as graph databases.
 * 
 * @author thomas.jungblut
 * 
 * @param <VERTEX_ID> the vertex id type.
 * @param <VERTEX_VALUE> the vertex value type.
 * @param <EDGE_VALUE> the edge value type.
 */
public interface Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

  /**
   * Adds a vertex with a list of adjacents to it.
   */
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      List<Edge<VERTEX_ID, EDGE_VALUE>> adjacents);

  /**
   * Adds a vertex with a single adjacent to it.
   */
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      Edge<VERTEX_ID, EDGE_VALUE> adjacent);

  /**
   * Adds a vertex with a list of adjacents to it.
   */
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      @SuppressWarnings("unchecked") Edge<VERTEX_ID, EDGE_VALUE>... adjacents);

  /**
   * Gets a set of adjacent vertices from a given vertex id.
   */
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(
      VERTEX_ID vertexId);

  /**
   * Get the vertex by its id.
   */
  public Vertex<VERTEX_ID, VERTEX_VALUE> getVertex(VERTEX_ID vertexId);

  /**
   * Gets a set of adjacent vertices from a given vertex.
   */
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(
      Vertex<VERTEX_ID, VERTEX_VALUE> vertex);

  /**
   * Gets all vertices associated with this graph.
   */
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getVertexSet();

  /**
   * Gets all verte IDs associated with this graph.
   */
  public Set<VERTEX_ID> getVertexIDSet();

  /**
   * Get's a set of edges outbound from the given vertex id.
   * 
   * @param vertexId the id of the vertex to get out edges.
   * @return a set of edges.
   */
  public Set<Edge<VERTEX_ID, EDGE_VALUE>> getEdges(VERTEX_ID vertexId);

  /**
   * Get's a set of edges outbound from the given vertex.
   * 
   * @param vertex the vertex to get out edges.
   * @return a set of edges.
   */
  public Set<Edge<VERTEX_ID, EDGE_VALUE>> getEdges(
      Vertex<VERTEX_ID, VERTEX_VALUE> vertex);

  /**
   * Finds an edge between the source and destination vertex.
   * 
   * @return null if nothing found, else the edge between those twos.
   */
  public Edge<VERTEX_ID, EDGE_VALUE> getEdge(VERTEX_ID source, VERTEX_ID dest);

  /**
   * @return how many vertices are present in this graph.
   */
  public int getNumVertices();

  /**
   * @return how many edges are present in this graph.
   */
  public int getNumEdges();

}
