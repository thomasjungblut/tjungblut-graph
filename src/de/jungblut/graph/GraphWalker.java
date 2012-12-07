package de.jungblut.graph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import de.jungblut.graph.model.Vertex;

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
   * 
   * @param <VERTEX_ID> the vertex id type.
   * @param <VERTEX_VALUE> the vertex value type.
   * @param <EDGE_VALUE> the edge value type.
   */
  public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>> Iterator<T> depthFirst(
      Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
    return new VertexIterator<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T>(true, g,
        start);
  }

  /**
   * Returns a breadth first search iterator on this graph from the given start
   * vertex.
   * 
   * @param <VERTEX_ID> the vertex id type.
   * @param <VERTEX_VALUE> the vertex value type.
   * @param <EDGE_VALUE> the edge value type.
   */
  public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>> Iterator<T> breadthFirst(
      Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
    return new VertexIterator<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T>(false, g,
        start);
  }

  /**
   * Vertex iterator class that is multifunction and can traverse depth and
   * breadth first be using two different methods of the {@link Deque}
   * interface.
   */
  private static final class VertexIterator<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>>
      extends AbstractIterator<T> {

    private final boolean depthFirst;
    private final Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g;

    private final Deque<VERTEX_ID> vertexDeque = new ArrayDeque<>();
    private final HashSet<VERTEX_ID> visitedVertices = new HashSet<>();

    public VertexIterator(boolean depthFirst,
        Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
      this.depthFirst = depthFirst;
      this.g = g;
      visitedVertices.add(start);
      vertexDeque.add(start);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final T computeNext() {
      if (!vertexDeque.isEmpty()) {
        VERTEX_ID currentVisit = null;
        // in DFS we will remove the first item on the "stack"
        if (depthFirst) {
          currentVisit = vertexDeque.pop();
        } else {
          // in BFS we will remove the first from the "queue"
          currentVisit = vertexDeque.poll();
        }
        // expand our known universe to the adjacent vertices
        for (Vertex<VERTEX_ID, VERTEX_VALUE> v : g
            .getAdjacentVertices(currentVisit)) {
          if (!visitedVertices.contains(v.getVertexId())) {
            if (depthFirst) {
              // in DFS we will add to the head
              vertexDeque.push(v.getVertexId());
            } else {
              // in DFS we will add to the tail
              vertexDeque.add(v.getVertexId());
            }
            visitedVertices.add(v.getVertexId());
          }
        }
        return (T) g.getVertex(currentVisit);
      } else {
        return endOfData();
      }
    }
  }

}
