package de.jungblut.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import de.jungblut.graph.vertex.Vertex;

public class AStar extends Dijkstra {

  public WeightedEdgeContainer startAStarSearch(Graph g, Vertex start,
      Vertex goal) {
    HashSet<Vertex> closedSet = new HashSet<Vertex>();
    HashSet<Vertex> openSet = new HashSet<Vertex>();
    HashMap<Vertex, Vertex> cameFrom = new HashMap<Vertex, Vertex>();
    // distance from the start from start along optimal path
    HashMap<Vertex, Integer> g_score = new HashMap<Vertex, Integer>();
    // distance from start to goal plus heuristic estimate
    HashMap<Vertex, Double> f_score = new HashMap<Vertex, Double>();
    // heuristic score
    HashMap<Vertex, Double> h_score = new HashMap<Vertex, Double>();
    g_score.put(start, 0);
    h_score.put(start, estimateDistance(g, start, goal));
    f_score.put(start, h_score.get(start));

    // create a deep copy
    for (Vertex v : g.getVertexSet())
      openSet.add(v);

    while (!openSet.isEmpty()) {
      Vertex v = findLowest(openSet, f_score);
      if (v.equals(goal)) {
        return new WeightedEdgeContainer(g_score, cameFrom, g, start);
      } else {
        openSet.remove(v);
        closedSet.add(v);
        for (Entry<Vertex, Integer> y : g.getAdjacentVertices(v).entrySet()) {
          boolean tentativeIsBetter = false;
          if (closedSet.contains(y.getKey()))
            continue;

          int tentativeGScore = g_score.get(v) + y.getValue();

          Integer gScore = g_score.get(y.getKey());
          if (!openSet.contains(y.getKey())) {
            openSet.add(y.getKey());
            tentativeIsBetter = true;
          } else if (gScore == null || tentativeGScore < gScore) {
            tentativeIsBetter = true;
          } else {
            tentativeIsBetter = false;
          }

          if (tentativeIsBetter) {
            cameFrom.put(y.getKey(), v);

            g_score.put(y.getKey(), tentativeGScore);
            double dist = estimateDistance(g, y.getKey(), goal);
            h_score.put(y.getKey(), dist);
            f_score.put(y.getKey(), tentativeGScore + dist);
          }
        }

      }
    }
    return null;
  }

  // the cost should never be over-estimated
  private double estimateDistance(Graph g, Vertex start, Vertex goal) {
    // get the average cost of all edges of start and goal
    double startWeight = averageEdgeWeights(g, start);
    double goalWeight = averageEdgeWeights(g, goal);
    // pow the result by 2
    double c = Math.pow((startWeight - goalWeight), 2);
    // absolute the distance
    return Math.abs(c - startWeight);
  }

  private double averageEdgeWeights(Graph g, Vertex vertex) {
    int sum = 0;
    int count = 0;
    Map<Vertex, Integer> adjacents = g.getAdjacentVertices(vertex);
    if (adjacents == null)
      return 0;
    for (Entry<Vertex, Integer> e : adjacents.entrySet()) {
      sum += e.getValue();
      count++;
    }
    if (count == 0)
      return 0;
    else
      return sum / count;
  }

  private Vertex findLowest(HashSet<Vertex> openSet,
      HashMap<Vertex, Double> scoredMap) {
    Vertex low = null;
    double lowest = Double.MAX_VALUE;
    for (Vertex v : openSet) {
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

}
