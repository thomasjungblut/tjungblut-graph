package de.jungblut.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

/**
 * Adjacency list implementation for sparse graphs like the webgraph. This is
 * simply implemented by a backing {@link HashMultimap}.
 * 
 * @author thomas.jungblut
 * 
 * @param <VERTEX_ID> the vertex id type.
 * @param <VERTEX_VALUE> the vertex value type.
 * @param <EDGE_VALUE> the edge value type.
 */
public final class AdjacencyList<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE>
    implements Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

  private final HashSet<Vertex<VERTEX_ID, VERTEX_VALUE>> vertexSet = new HashSet<>();
  private final HashMap<VERTEX_ID, Vertex<VERTEX_ID, VERTEX_VALUE>> vertexMap = new HashMap<>();
  private final HashMultimap<VERTEX_ID, VERTEX_ID> adjacencyMultiMap = HashMultimap
      .create();
  private final HashMultimap<VERTEX_ID, Edge<VERTEX_ID, EDGE_VALUE>> edgeMultiMap = HashMultimap
      .create();

  @Override
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(
      VERTEX_ID vertexId) {
    Set<VERTEX_ID> set = adjacencyMultiMap.get(vertexId);
    Set<Vertex<VERTEX_ID, VERTEX_VALUE>> toReturn = Sets.newHashSet();
    for (VERTEX_ID id : set) {
      toReturn.add(getVertex(id));
    }
    return toReturn;
  }

  @Override
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(
      Vertex<VERTEX_ID, VERTEX_VALUE> vertex) {
    return getAdjacentVertices(vertex.getVertexId());
  }

  @Override
  public Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getVertexSet() {
    return vertexSet;
  }

  @Override
  public Set<VERTEX_ID> getVertexIDSet() {
    return vertexMap.keySet();
  }

  @Override
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      @SuppressWarnings("unchecked") Edge<VERTEX_ID, EDGE_VALUE>... adjacents) {
    for (Edge<VERTEX_ID, EDGE_VALUE> edge : adjacents) {
      addVertex(vertex, edge);
    }
  }

  @Override
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      List<Edge<VERTEX_ID, EDGE_VALUE>> adjacents) {
    for (Edge<VERTEX_ID, EDGE_VALUE> edge : adjacents) {
      addVertex(vertex, edge);
    }
  }

  @Override
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex) {
    vertexSet.add(vertex);
    vertexMap.put(vertex.getVertexId(), vertex);
  }

  @Override
  public void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
      Edge<VERTEX_ID, EDGE_VALUE> adjacent) {
    addVertex(vertex);
    addEdge(vertex.getVertexId(), adjacent);
  }

  @Override
  public void addEdge(VERTEX_ID vertexId, Edge<VERTEX_ID, EDGE_VALUE> edge) {
    edgeMultiMap.put(vertexId, edge);
    adjacencyMultiMap.put(vertexId, edge.getDestinationVertexID());
  }

  @Override
  public Set<Edge<VERTEX_ID, EDGE_VALUE>> getEdges(VERTEX_ID vertexId) {
    return edgeMultiMap.get(vertexId);
  }

  @Override
  public Set<Edge<VERTEX_ID, EDGE_VALUE>> getEdges(
      Vertex<VERTEX_ID, VERTEX_VALUE> vertex) {
    return getEdges(vertex.getVertexId());
  }

  @Override
  public Edge<VERTEX_ID, EDGE_VALUE> getEdge(VERTEX_ID source, VERTEX_ID dest) {
    Set<Edge<VERTEX_ID, EDGE_VALUE>> set = edgeMultiMap.get(source);
    for (Edge<VERTEX_ID, EDGE_VALUE> e : set) {
      if (e.getDestinationVertexID().equals(dest))
        return e;
    }
    return null;
  }

  @Override
  public Vertex<VERTEX_ID, VERTEX_VALUE> getVertex(VERTEX_ID vertexId) {
    return vertexMap.get(vertexId);
  }

  @Override
  public int getNumVertices() {
    return vertexSet.size();
  }

  @Override
  public int getNumEdges() {
    return adjacencyMultiMap.size();
  }

  @Override
  public String toString() {
    return "AdjacencyList [getNumVertices()=" + this.getNumVertices()
        + ", getNumEdges()=" + this.getNumEdges() + "]";
  }

}
