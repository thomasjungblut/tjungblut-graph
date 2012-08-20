package de.jungblut.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import de.jungblut.graph.vertex.Vertex;

/**
 * Graph walker utility classes for most common walks like DFS and BFS.
 * 
 * @author thomas.jungblut
 * 
 */
public final class GraphWalker {

  /**
   * Returns a depth first search iterator on this graph from the given start
   * vertex.
   */
  public static <T extends Vertex> Iterator<T> depthFirst(Graph<T> g, T start) {
    return new VertexIterator<T>(true, g, start);
  }

  /**
   * Returns a breadth first search iterator on this graph from the given start
   * vertex.
   */
  public static <T extends Vertex> Iterator<T> breadthFirst(Graph<T> g, T start) {
    return new VertexIterator<T>(false, g, start);
  }

  /**
   * Vertex iterator class that is multifunction and can traverse depth and
   * breadth first be using two different methods of the {@link Deque}
   * interface.
   * 
   * @author thomas.jungblut
   * 
   */
  private static final class VertexIterator<T extends Vertex> extends
      AbstractIterator<T> {

    private final boolean depthFirst;
    private final Graph<T> g;

    private final Deque<T> vertexDeque = new ArrayDeque<T>();
    private final HashSet<T> visitedVertices = new HashSet<T>();

    public VertexIterator(boolean depthFirst, Graph<T> g, T start) {
      this.depthFirst = depthFirst;
      this.g = g;
      visitedVertices.add(start);
      vertexDeque.add(start);
    }

    @Override
    protected final T computeNext() {
      if (!vertexDeque.isEmpty()) {
        T currentVisit = null;
        // in DFS we will remove the first item on the "stack"
        if (depthFirst) {
          currentVisit = vertexDeque.pop();
        } else {
          // in BFS we will remove the first from the "queue"
          currentVisit = vertexDeque.poll();
        }
        // expand our known universe to the adjacent vertices
        for (T v : g.getAdjacentVertices(currentVisit.getVertexId())) {
          if (!visitedVertices.contains(v)) {
            if (depthFirst) {
              // in DFS we will add to the head
              vertexDeque.push(v);
            } else {
              // in DFS we will add to the tail
              vertexDeque.add(v);
            }
            visitedVertices.add(v);
          }
        }
        return currentVisit;
      } else {
        return endOfData();
      }
    }
  }

}
