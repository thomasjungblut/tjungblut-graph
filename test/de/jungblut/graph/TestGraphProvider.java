package de.jungblut.graph;

import java.util.ArrayList;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.VertexImpl;

public class TestGraphProvider {

  /**
   * from {@link http://de.wikipedia.org/wiki/Dijkstra-Algorithmus#Beispiel}
   */
  @SuppressWarnings("unchecked")
  public static Graph<Integer, String, Integer> getWikipediaExampleGraph() {
    Graph<Integer, String, Integer> graph = new AdjacencyList<>();

    ArrayList<VertexImpl<Integer, String>> cities = new ArrayList<>();

    cities.add(new VertexImpl<Integer, String>(0, "Frankfurt"));
    cities.add(new VertexImpl<Integer, String>(1, "Mannheim"));
    cities.add(new VertexImpl<Integer, String>(2, "Wuerzburg"));
    cities.add(new VertexImpl<Integer, String>(3, "Stuttgart"));
    cities.add(new VertexImpl<Integer, String>(4, "Kassel"));
    cities.add(new VertexImpl<Integer, String>(5, "Karlsruhe"));
    cities.add(new VertexImpl<Integer, String>(6, "Erfurt"));
    cities.add(new VertexImpl<Integer, String>(7, "Nuernberg"));
    cities.add(new VertexImpl<Integer, String>(8, "Augsburg"));
    cities.add(new VertexImpl<Integer, String>(9, "Muenchen"));

    // frankfurt -> mannheim, kassel, wuerzburg
    graph.addVertex(cities.get(0), new Edge<Integer, Integer>(cities.get(1)
        .getVertexId(), 85), new Edge<Integer, Integer>(cities.get(4)
        .getVertexId(), 173), new Edge<Integer, Integer>(cities.get(2)
        .getVertexId(), 217));

    // mannheim -> karlsruhe, frankfurt
    graph.addVertex(cities.get(1), new Edge<Integer, Integer>(cities.get(0)
        .getVertexId(), 85), new Edge<Integer, Integer>(cities.get(5)
        .getVertexId(), 80));

    // wuerzburg -> erfurt, frankfurt, nuernberg
    graph.addVertex(cities.get(2), new Edge<Integer, Integer>(cities.get(0)
        .getVertexId(), 217), new Edge<Integer, Integer>(cities.get(6)
        .getVertexId(), 186), new Edge<Integer, Integer>(cities.get(7)
        .getVertexId(), 103));

    // stuttgart -> nuernberg
    graph.addVertex(cities.get(3), new Edge<Integer, Integer>(cities.get(7)
        .getVertexId(), 183));

    // kassel -> muenchen
    graph.addVertex(cities.get(4), new Edge<Integer, Integer>(cities.get(9)
        .getVertexId(), 502));

    // karlsruhe -> mannheim, augsburg
    graph.addVertex(cities.get(5), new Edge<Integer, Integer>(cities.get(8)
        .getVertexId(), 250), new Edge<Integer, Integer>(cities.get(1)
        .getVertexId(), 80));

    // erfurt -> wuerzburg
    graph.addVertex(cities.get(6), new Edge<Integer, Integer>(cities.get(2)
        .getVertexId(), 186));

    // nuernberg -> stuttgart, muenchen, wuerzburg
    graph.addVertex(cities.get(7), new Edge<Integer, Integer>(cities.get(3)
        .getVertexId(), 183), new Edge<Integer, Integer>(cities.get(9)
        .getVertexId(), 167), new Edge<Integer, Integer>(cities.get(2)
        .getVertexId(), 103));

    // augsburg -> karlsruhe, muenchen
    graph.addVertex(cities.get(8), new Edge<Integer, Integer>(cities.get(5)
        .getVertexId(), 250), new Edge<Integer, Integer>(cities.get(9)
        .getVertexId(), 84));

    // muenchen -> nuernberg, kassel, augsburg
    graph.addVertex(cities.get(9),

    new Edge<Integer, Integer>(cities.get(7).getVertexId(), 167),
        new Edge<Integer, Integer>(cities.get(4).getVertexId(), 173),
        new Edge<Integer, Integer>(cities.get(8).getVertexId(), 84));

    return graph;
  }
}
