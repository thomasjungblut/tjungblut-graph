package de.jungblut.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;

import de.jungblut.graph.vertex.Vertex;

/**
 * Adjacency list implementation for sparse graphs like the webgraph. This is
 * simply implemented by a backing {@link HashMultimap}.
 * 
 * @author thomas.jungblut
 * 
 * @param <V> the type of {@link Vertex} to use.
 */
public final class AdjacencyList<V extends Vertex> implements Graph<V> {

  private final HashSet<V> vertexSet = new HashSet<>();
  private final HashMap<Integer, V> vertexMap = new HashMap<>();
  private final HashMultimap<Integer, V> adjacencyList = HashMultimap.create();

  @Override
  public Set<V> getAdjacentVertices(int vertexId) {
    return adjacencyList.get(vertexId);
  }

  @Override
  public Set<V> getAdjacentVertices(V vertex) {
    return adjacencyList.get(vertex.getVertexId());
  }

  @Override
  public Set<V> getVertexSet() {
    return vertexSet;
  }

  @Override
  public void addVertex(V vertex, @SuppressWarnings("unchecked") V... adjacents) {
    addVertex(vertex, Arrays.asList(adjacents));
  }

  @Override
  public void addVertex(V vertex, List<V> adjacents) {
    vertexMap.put(vertex.getVertexId(), vertex);
    vertexSet.addAll(adjacents);
    adjacencyList.putAll(vertex.getVertexId(), adjacents);
  }

  @Override
  public V getVertex(int vertexId) {
    return vertexMap.get(vertexId);
  }

}
