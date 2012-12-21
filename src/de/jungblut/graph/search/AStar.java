package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

/**
 * A* algorithm for fast and heuristic search of the shortest paths in a graph.
 * 
 * @author thomas.jungblut
 */
public final class AStar<VERTEX_ID, VERTEX_VALUE> {

  private final DistanceMeasurer<VERTEX_ID, VERTEX_VALUE, Integer> DEFAULT_MEASURER = new DefaultDistanceMeasurer();

  /**
   * Executes an A* search in a graph. It needs a graph, the start and end
   * vertex. The heuristic distance measurer is based on the average edge weight
   * of start and end node.
   */
  public WeightedEdgeContainer<VERTEX_ID, Integer> startAStarSearch(
      Graph<VERTEX_ID, VERTEX_VALUE, Integer> g, VERTEX_ID start, VERTEX_ID goal) {
    return startAStarSearch(g, start, goal, DEFAULT_MEASURER);
  }

  /**
   * Executes an A* search in a graph. It needs a graph, the start and end
   * vertex as well as a heuristic distance measurer.
   */
  public WeightedEdgeContainer<VERTEX_ID, Integer> startAStarSearch(
      Graph<VERTEX_ID, VERTEX_VALUE, Integer> g, VERTEX_ID start,
      VERTEX_ID goal,
      DistanceMeasurer<VERTEX_ID, VERTEX_VALUE, Integer> measurer) {

    HashSet<VERTEX_ID> closedSet = new HashSet<>();
    HashSet<VERTEX_ID> openSet = new HashSet<>();
    HashMap<VERTEX_ID, VERTEX_ID> cameFrom = new HashMap<>();
    // distance from the start from start along optimal path
    HashMap<VERTEX_ID, Integer> g_score = new HashMap<>();
    // distance from start to goal plus heuristic estimate
    HashMap<VERTEX_ID, Double> f_score = new HashMap<>();
    // heuristic score
    HashMap<VERTEX_ID, Double> h_score = new HashMap<>();
    g_score.put(start, 0);
    h_score.put(start, measurer.measureDistance(g, start, goal));
    f_score.put(start, h_score.get(start));

    // create a deep copy
    for (Vertex<VERTEX_ID, VERTEX_VALUE> v : g.getVertexSet())
      openSet.add(v.getVertexId());

    while (!openSet.isEmpty()) {
      VERTEX_ID v = findLowest(openSet, f_score);
      if (v.equals(goal)) {
        return new WeightedEdgeContainer<VERTEX_ID, Integer>(g_score, cameFrom);
      } else {
        openSet.remove(v);
        closedSet.add(v);
        for (Edge<VERTEX_ID, Integer> y : g.getEdges(v)) {
          boolean tentativeIsBetter = false;
          if (closedSet.contains(y.getDestinationVertexID()))
            continue;

          int tentativeGScore = g_score.get(v) + y.getValue();

          Integer gScore = g_score.get(y);
          if (!openSet.contains(y.getDestinationVertexID())) {
            openSet.add(y.getDestinationVertexID());
            tentativeIsBetter = true;
          } else if (gScore == null || tentativeGScore < gScore) {
            tentativeIsBetter = true;
          } else {
            tentativeIsBetter = false;
          }

          if (tentativeIsBetter) {
            cameFrom.put(y.getDestinationVertexID(), v);

            g_score.put(y.getDestinationVertexID(), tentativeGScore);
            double dist = measurer.measureDistance(g,
                y.getDestinationVertexID(), goal);
            h_score.put(y.getDestinationVertexID(), dist);
            f_score.put(y.getDestinationVertexID(), tentativeGScore + dist);
          }
        }

      }
    }
    return new WeightedEdgeContainer<>(g_score, cameFrom);
  }

  private VERTEX_ID findLowest(HashSet<VERTEX_ID> openSet,
      HashMap<VERTEX_ID, Double> scoredMap) {
    VERTEX_ID low = null;
    double lowest = Double.MAX_VALUE;
    for (VERTEX_ID v : openSet) {
      Double current = scoredMap.get(v);
      if (low == null && current != null) {
        low = v;
        lowest = current;
      } else {
        if (current != null && lowest > current) {
          low = v;
          lowest = current;
        }
      }
    }
    return low;
  }

  /**
   * That does not make so much sense, but works for small graphs because it
   * never over estimates the cost.
   * 
   * @author thomas.jungblut
   * 
   */
  private final class DefaultDistanceMeasurer implements
      DistanceMeasurer<VERTEX_ID, VERTEX_VALUE, Integer> {

    @Override
    public double measureDistance(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g,
        VERTEX_ID start, VERTEX_ID goal) {
      double startWeight = averageEdgeWeights(g, start);
      double goalWeight = averageEdgeWeights(g, goal);
      double diff = startWeight - goalWeight;
      // pow the result by 2
      double c = diff * diff;
      // absolute the distance
      return Math.abs(c - startWeight);
    }

    private double averageEdgeWeights(
        Graph<VERTEX_ID, VERTEX_VALUE, Integer> g, VERTEX_ID vertex) {
      int sum = 0;
      int count = 0;
      Set<Edge<VERTEX_ID, Integer>> adjacents = g.getEdges(vertex);
      if (adjacents == null)
        return 0;
      for (Edge<VERTEX_ID, Integer> e : adjacents) {
        sum += e.getValue();
        count++;
      }
      if (count == 0)
        return 0;
      else
        return sum / count;
    }
  }

  public static final <VERTEX_ID, VERTEX_VALUE> AStar<VERTEX_ID, VERTEX_VALUE> getInstance() {
    return new AStar<VERTEX_ID, VERTEX_VALUE>();
  }

}
