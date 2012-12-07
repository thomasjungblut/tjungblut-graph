package de.jungblut.graph.mst;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.IdCostTuple;

/**
 * Implementation of Prim's algorithm with a {@link PriorityQueue}.
 * 
 * @author thomas.jungblut
 * 
 */
public final class Prim<VERTEX_ID, VERTEX_VALUE> {

  /**
   * Construct a minimum spanning tree with prims algorithm enhanced with a heap
   * to offer logarithmic complexity.
   * 
   * @param graph the graph to calculate a MST from.
   * @param start the start vertex.
   * @return a new {@link AdjacencyList} that contains the minimum spanning
   *         tree.
   */
  public Graph<VERTEX_ID, VERTEX_VALUE, Integer> constructMinimumSpanningTree(
      Graph<VERTEX_ID, VERTEX_VALUE, Integer> graph, VERTEX_ID start) {

    Graph<VERTEX_ID, VERTEX_VALUE, Integer> mst = new AdjacencyList<>();
    PriorityQueue<IdCostTuple<VERTEX_ID>> edgeQueue = new PriorityQueue<>();
    Set<Edge<VERTEX_ID, Integer>> edges = graph.getEdges(start);
    // copy to not mutate underlying structure
    Set<VERTEX_ID> vertexIDSet = new HashSet<>(graph.getVertexIDSet());

    mst.addVertex(graph.getVertex(start));
    for (Edge<VERTEX_ID, Integer> edge : edges) {
      edgeQueue.add(new IdCostTuple<>(edge.getDestinationVertexID(), start,
          edge.getValue()));
    }

    while (!vertexIDSet.isEmpty()) {
      IdCostTuple<VERTEX_ID> minEdge = edgeQueue.poll();
      // prevent infinity loops when edges are empty but not all vertices are
      // added (multiple components)
      if (minEdge == null) {
        break;
      }
      // check if the source is in the MST and if the destination is not there
      // to prevent loops and destroy the tree property
      if (mst.getVertex(minEdge.getSource()) != null
          && mst.getVertex(minEdge.getVertexId()) == null) {
        mst.addVertex(graph.getVertex(minEdge.getVertexId()));
        mst.addEdge(
            minEdge.getSource(),
            new Edge<VERTEX_ID, Integer>(minEdge.getVertexId(), minEdge
                .getDistance()));

        // add the edges of the destination to the priority queue
        edges = graph.getEdges(minEdge.getVertexId());
        for (Edge<VERTEX_ID, Integer> edge : edges) {
          edgeQueue.add(new IdCostTuple<>(edge.getDestinationVertexID(),
              minEdge.getVertexId(), edge.getValue()));
        }

      }

    }

    return mst;
  }

  public static final <VERTEX_ID, VERTEX_VALUE> Prim<VERTEX_ID, VERTEX_VALUE> getInstance() {
    return new Prim<VERTEX_ID, VERTEX_VALUE>();
  }

}
