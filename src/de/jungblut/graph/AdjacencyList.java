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
 * @param <K> the type of {@link Vertex} to use.
 */
public final class AdjacencyList<K extends Vertex> implements Graph<K> {

  private final HashSet<K> vertexSet = new HashSet<>();
  private final HashMap<Integer, K> vertexMap = new HashMap<>();
  private final HashMultimap<Integer, K> adjacencyList = HashMultimap.create();

  @Override
  public Set<K> getAdjacentVertices(int vertexId) {
    return adjacencyList.get(vertexId);
  }

  @Override
  public Set<K> getAdjacentVertices(K vertex) {
    return adjacencyList.get(vertex.getVertexId());
  }

  @Override
  public Set<K> getVertexSet() {
    return vertexSet;
  }

  @Override
  public void addVertex(K vertex, @SuppressWarnings("unchecked") K... adjacents) {
    addVertex(vertex, Arrays.asList(adjacents));
  }

  @Override
  public void addVertex(K vertex, List<K> adjacents) {
    vertexMap.put(vertex.getVertexId(), vertex);
    vertexSet.addAll(adjacents);
    adjacencyList.putAll(vertex.getVertexId(), adjacents);
  }

  @Override
  public K getVertex(int vertexId) {
    return vertexMap.get(vertexId);
  }

}
