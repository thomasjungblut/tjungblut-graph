package de.jungblut.graph.sort;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Vertex;

/**
 * Topological sort, that sorts vertices in a directed acylic graph such that
 * for every directed edge uv from vertex u to vertex v, u comes before v in the
 * ordering.
 * 
 * @author thomas.jungblut
 * 
 */
public final class TopologicalSort {

  private TopologicalSort() {
    throw new IllegalAccessError();
  }

  /**
   * Sorts the graph's vertices topological with Thomas Cormen's algorithm
   * (2001).
   * 
   * @param g the graph to sort.
   * @return null if the graph contained cycles, or a list of vertices in
   *         ascending order.
   */
  public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> List<Vertex<VERTEX_ID, VERTEX_VALUE>> sort(
      Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g) {

    List<Vertex<VERTEX_ID, VERTEX_VALUE>> solution = Lists.newArrayList();
    Set<VERTEX_ID> tmpMarkSet = Sets.newHashSet();
    Set<VERTEX_ID> unmarked = new HashSet<>(g.getVertexIDSet());
    while (!unmarked.isEmpty()) {
      VERTEX_ID next = unmarked.iterator().next();
      // if we found a cycle, break
      if (visit(next, g, solution, unmarked, tmpMarkSet)) {
        return null;
      }
    }
    Collections.reverse(solution);
    return solution;
  }

  /**
   * Visit function that is a modified BFS.
   * 
   * @param current the current vertex to visit.
   * @param g the graph.
   * @param solution the solution that can be extended.
   * @param unmarked the unmarked vertices.
   * @param tmpMarked the temporary marked vertices for every recursion step.
   * @return true if found a cycle, false if not.
   */
  private static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> boolean visit(
      VERTEX_ID current, Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g,
      List<Vertex<VERTEX_ID, VERTEX_VALUE>> solution, Set<VERTEX_ID> unmarked,
      Set<VERTEX_ID> tmpMarked) {
    // if we have marked it temporary, we have found a cycle
    if (tmpMarked.contains(current)) {
      return true;
    }
    // if is unmarked
    if (unmarked.contains(current)) {
      // mark it temporary
      tmpMarked.add(current);
      Set<Vertex<VERTEX_ID, VERTEX_VALUE>> adjacentVertices = g
          .getAdjacentVertices(current);
      // loop over all outgoing edges
      for (Vertex<VERTEX_ID, VERTEX_VALUE> adjacent : adjacentVertices) {
        boolean cycle = visit(adjacent.getVertexId(), g, solution, unmarked,
            tmpMarked);
        // escape the recursion when we found a cycle
        if (cycle) {
          return true;
        }
      }
      // mark permanently
      unmarked.remove(current);
      // remove from temporary marks
      tmpMarked.remove(current);
      solution.add(g.getVertex(current));
    }
    return false;
  }

}
