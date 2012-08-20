package de.jungblut.graph;

import de.jungblut.graph.vertex.CostVertex;
import de.jungblut.graph.vertex.NamedVertex;

public class TestGraphProvider {

  /**
   * from {@link http://de.wikipedia.org/wiki/Dijkstra-Algorithmus#Beispiel}
   */
  public static Graph<CostVertex> getWikipediaExampleGraph() {
    Graph<CostVertex> graph = new AdjacencyList<>();

    CostVertex[] cities = new CostVertex[] {
        new CostVertex(new NamedVertex(0, "Frankfurt"), 0),
        new CostVertex(new NamedVertex(1, "Mannheim"), 0),
        new CostVertex(new NamedVertex(2, "Wuerzburg"), 0),
        new CostVertex(new NamedVertex(3, "Stuttgart"), 0),
        new CostVertex(new NamedVertex(4, "Kassel"), 0),
        new CostVertex(new NamedVertex(5, "Karlsruhe"), 0),
        new CostVertex(new NamedVertex(6, "Erfurt"), 0),
        new CostVertex(new NamedVertex(7, "Nuernberg"), 0),
        new CostVertex(new NamedVertex(8, "Augsburg"), 0),
        new CostVertex(new NamedVertex(9, "Muenchen"), 0) };

    // frankfurt -> mannheim, kassel, wuerzburg
    graph.addVertex(cities[0], new CostVertex(cities[1], 85), new CostVertex(
        cities[4], 173), new CostVertex(cities[2], 217));

    // mannheim -> karlsruhe, frankfurt
    graph.addVertex(cities[1], new CostVertex(cities[0], 85), new CostVertex(
        cities[5], 80));

    // wuerzburg -> erfurt, frankfurt, nuernberg
    graph.addVertex(cities[2], new CostVertex(cities[0], 217), new CostVertex(
        cities[6], 186), new CostVertex(cities[7], 103));

    // stuttgart -> nuernberg
    graph.addVertex(cities[3], new CostVertex(cities[7], 183));

    // kassel -> muenchen
    graph.addVertex(cities[4], new CostVertex(cities[9], 502));

    // karlsruhe -> mannheim, augsburg
    graph.addVertex(cities[5], new CostVertex(cities[8], 250), new CostVertex(
        cities[1], 80));

    // erfurt -> wuerzburg
    graph.addVertex(cities[6], new CostVertex(cities[2], 186));

    // nuernberg -> stuttgart, muenchen, wuerzburg
    graph.addVertex(cities[7], new CostVertex(cities[3], 183), new CostVertex(
        cities[9], 167), new CostVertex(cities[2], 103));

    // augsburg -> karlsruhe, muenchen
    graph.addVertex(cities[8], new CostVertex(cities[5], 250), new CostVertex(
        cities[9], 84));

    // muenchen -> nuernberg, kassel, augsburg
    graph.addVertex(cities[9], new CostVertex(cities[7], 167), new CostVertex(
        cities[4], 173), new CostVertex(cities[8], 84));

    return graph;
  }
}
