package de.jungblut.graph;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import de.jungblut.graph.vertex.Vertex;

public final class GraphWalker {

  /*
   * TODO return an iterable on these
   */

  public static void depthFirst(Graph g, Vertex start) {
    Deque<Vertex> vertexStack = new LinkedList<Vertex>();
    HashSet<Vertex> visitedVertices = new HashSet<Vertex>();

    visitedVertices.add(start);
    vertexStack.add(start);
    while (!vertexStack.isEmpty()) {
      Vertex currentVisit = vertexStack.pop();
      for (Vertex v : g.getAdjacentVertices(currentVisit).keySet()) {
        if (!visitedVertices.contains(v)) {
          vertexStack.push(v);
          visitedVertices.add(v);
        }
      }
    }
  }

  public static void breadthFirst(Graph g, Vertex start) {
    Queue<Vertex> vertexQueue = new LinkedList<Vertex>();
    HashSet<Vertex> visitedVertices = new HashSet<Vertex>();

    visitedVertices.add(start);
    vertexQueue.add(start);
    while (!vertexQueue.isEmpty()) {
      Vertex currentVisit = vertexQueue.poll();
      for (Vertex v : g.getAdjacentVertices(currentVisit).keySet()) {
        if (!visitedVertices.contains(v)) {
          vertexQueue.add(v);
          visitedVertices.add(v);
        }
      }
    }

  }

}
