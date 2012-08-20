package de.jungblut.graph;

import java.util.HashSet;
import java.util.Map;

import de.jungblut.graph.vertex.Vertex;

public interface Graph {

  public Map<Vertex, Integer> getAdjacentVertices(Vertex v);

  public HashSet<Vertex> getVertexSet();

}
