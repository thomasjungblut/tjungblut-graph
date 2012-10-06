package de.jungblut.graph.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.CostVertex;

/**
 * A* algorithm for fast and heuristic search of the shortest paths in a graph.
 * 
 * @author thomas.jungblut
 * 
 */
public final class AStar {

  private static final DistanceMeasurer<CostVertex> DEFAULT_MEASURER = new DefaultDistanceMeasurer();

  /**
   * Executes an A* search in a graph. It needs a graph, the start and end
   * vertex. The heuristic distance measurer is based on the average edge weight
   * of start and end node.
   */
  public static WeightedEdgeContainer<CostVertex> startAStarSearch(
      Graph<CostVertex> g, CostVertex start, CostVertex goal) {
    return startAStarSearch(g, start, goal, DEFAULT_MEASURER);
  }

  /**
   * Executes an A* search in a graph. It needs a graph, the start and end
   * vertex as well as a heuristic distance measurer.
   */
  public static WeightedEdgeContainer<CostVertex> startAStarSearch(
      Graph<CostVertex> g, CostVertex start, CostVertex goal,
      DistanceMeasurer<CostVertex> measurer) {

    HashSet<CostVertex> closedSet = new HashSet<>();
    HashSet<CostVertex> openSet = new HashSet<>();
    HashMap<CostVertex, CostVertex> cameFrom = new HashMap<>();
    // distance from the start from start along optimal path
    HashMap<CostVertex, Integer> g_score = new HashMap<>();
    // distance from start to goal plus heuristic estimate
    HashMap<CostVertex, Double> f_score = new HashMap<>();
    // heuristic score
    HashMap<CostVertex, Double> h_score = new HashMap<>();
    g_score.put(start, 0);
    h_score.put(start, measurer.measureDistance(g, start, goal));
    f_score.put(start, h_score.get(start));

    // create a deep copy
    for (CostVertex v : g.getVertexSet())
      openSet.add(v);

    while (!openSet.isEmpty()) {
      CostVertex v = findLowest(openSet, f_score);
      if (v.equals(goal)) {
        return new WeightedEdgeContainer<CostVertex>(g_score, cameFrom);
      } else {
        openSet.remove(v);
        closedSet.add(v);
        for (CostVertex y : g.getAdjacentVertices(v.getVertexId())) {
          boolean tentativeIsBetter = false;
          if (closedSet.contains(y))
            continue;

          int tentativeGScore = g_score.get(v) + y.getCost();

          Integer gScore = g_score.get(y);
          if (!openSet.contains(y)) {
            openSet.add(y);
            tentativeIsBetter = true;
          } else if (gScore == null || tentativeGScore < gScore) {
            tentativeIsBetter = true;
          } else {
            tentativeIsBetter = false;
          }

          if (tentativeIsBetter) {
            cameFrom.put(y, v);

            g_score.put(y, tentativeGScore);
            double dist = measurer.measureDistance(g, y, goal);
            h_score.put(y, dist);
            f_score.put(y, tentativeGScore + dist);
          }
        }

      }
    }
    return new WeightedEdgeContainer<>(g_score, cameFrom);
  }

  private static CostVertex findLowest(HashSet<CostVertex> openSet,
      HashMap<CostVertex, Double> scoredMap) {
    CostVertex low = null;
    double lowest = Double.MAX_VALUE;
    for (CostVertex v : openSet) {
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
  private static class DefaultDistanceMeasurer implements
      DistanceMeasurer<CostVertex> {

    @Override
    public double measureDistance(Graph<CostVertex> g, CostVertex start,
        CostVertex goal) {
      double startWeight = averageEdgeWeights(g, start);
      double goalWeight = averageEdgeWeights(g, goal);
      // pow the result by 2
      double c = Math.pow((startWeight - goalWeight), 2);
      // absolute the distance
      return Math.abs(c - startWeight);
    }

    private double averageEdgeWeights(Graph<CostVertex> g, CostVertex vertex) {
      int sum = 0;
      int count = 0;
      Set<CostVertex> adjacents = g.getAdjacentVertices(vertex);
      if (adjacents == null)
        return 0;
      for (CostVertex e : adjacents) {
        sum += e.getCost();
        count++;
      }
      if (count == 0)
        return 0;
      else
        return sum / count;
    }
  };

}
