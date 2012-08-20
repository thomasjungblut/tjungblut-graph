package de.jungblut.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import de.jungblut.graph.vertex.BasicVertex;
import de.jungblut.graph.vertex.Vertex;

public class Dijkstra {

  WeightedEdgeContainer shortestPaths;

  protected Dijkstra() {
  }

  public Dijkstra(Graph g, Vertex start) {
    long startTime = System.currentTimeMillis();
    shortestPaths = findShortestPaths(g, start);
    long endTime = System.currentTimeMillis();
    // printShortestPaths(shortestPaths, true);
    System.out.println("Took " + (endTime - startTime) + "ms to dijkstra!");
    // shortestPathInteractivly();
  }

  protected void printShortestPaths(WeightedEdgeContainer e, boolean withCosts) {
    for (Entry<Vertex, Vertex> entry : e.ancestors.entrySet()) {
      if (withCosts)
        System.out.println("To " + entry.getKey() + " with a cost of: "
            + e.path.get(entry.getKey()));
      else
        System.out.println("\t" + entry.getKey());
    }
  }

  protected void printShortestPath(Stack<Vertex> pathStack) {
    while (!pathStack.isEmpty()) {
      System.out.println("\t" + pathStack.pop());
    }
  }

  protected Stack<Vertex> reconstructShortestPath(
      WeightedEdgeContainer container, Vertex destination) {
    Stack<Vertex> pathStack = new Stack<Vertex>();
    Vertex current = destination;
    while (current != null) {
      pathStack.push(current);
      current = container.ancestors.get(current);
    }
    return pathStack;
  }

  protected void initialize(Graph g, Vertex start,
      Map<Vertex, Integer> pathMap, Map<Vertex, Vertex> ancestors,
      HashSet<Vertex> vertizes) {
    // initialize the matrix with infinity = max_value
    for (Vertex v : g.getVertexSet()) {
      pathMap.put(v, Integer.valueOf(Integer.MAX_VALUE));
      ancestors.put(v, v);
      vertizes.add(new BasicVertex(v.getVertexId()));
    }
    // set the distance from start to start to zero
    pathMap.put(start, Integer.valueOf(0));
    // set the ancestors for start to null, cause it never gets them
    ancestors.put(start, null);
  }

  protected void updateDistance(Graph g, Vertex u, Vertex v, Integer weight,
      HashMap<Vertex, Integer> path, HashMap<Vertex, Vertex> ancestors) {
    int summedWeight = path.get(u) + weight;
    if (summedWeight < path.get(v)) {
      path.put(v, summedWeight);
      ancestors.put(v, u);
    }
  }

  protected WeightedEdgeContainer findShortestPaths(Graph g, Vertex start) {
    // some datastructure needed
    HashMap<Vertex, Integer> path = new HashMap<Vertex, Integer>();
    HashMap<Vertex, Vertex> ancestors = new HashMap<Vertex, Vertex>();
    HashSet<Vertex> vertizes = new HashSet<Vertex>();
    initialize(g, start, path, ancestors, vertizes);
    // main algorithm
    while (!vertizes.isEmpty()) {
      Vertex u = findLowestWeight(vertizes, path);
      vertizes.remove(u);
      for (Entry<Vertex, Integer> v : g.getAdjacentVertices(u).entrySet()) {
        if (vertizes.contains(v.getKey())) {
          updateDistance(g, u, v.getKey(), v.getValue(), path, ancestors);
        }
      }
    }
    return new WeightedEdgeContainer(path, ancestors, g, start);
  }

  protected Vertex findLowestWeight(HashSet<Vertex> q,
      HashMap<Vertex, Integer> path) {
    Vertex lowest = null;
    int currentLowest = -1;
    for (Vertex v : q) {
      if (lowest == null) {
        lowest = v;
        currentLowest = path.get(lowest);
      } else {
        Integer u = path.get(v);
        if (u.intValue() < currentLowest) {
          lowest = v;
          currentLowest = u;
        }
      }
    }
    return lowest;
  }

  class WeightedEdgeContainer {

    Graph g;
    Vertex start;
    HashMap<Vertex, Integer> path;
    HashMap<Vertex, Vertex> ancestors;

    public WeightedEdgeContainer(HashMap<Vertex, Integer> path,
        HashMap<Vertex, Vertex> ancestors, Graph g, Vertex start) {
      super();
      this.path = path;
      this.ancestors = ancestors;
      this.g = g;
      this.start = start;
    }
  }

}
