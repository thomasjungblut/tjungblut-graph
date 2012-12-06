package de.jungblut.graph;

import de.jungblut.graph.vertex.CostVertex;

public class TestGraphProvider {

  /**
   * from {@link http://de.wikipedia.org/wiki/Dijkstra-Algorithmus#Beispiel}
   */
  public static Graph<CostVertex> getWikipediaExampleGraph() {
    Graph<CostVertex> graph = new AdjacencyList<>();

    CostVertex[] cities = new CostVertex[] { new CostVertex(0, 0),// "Frankfurt"
        new CostVertex(1, 0),// "Mannheim"),
        new CostVertex(2, 0), // "Wuerzburg"),
        new CostVertex(3, 0), // "Stuttgart"),
        new CostVertex(4, 0), // "Kassel"),
        new CostVertex(5, 0), // "Karlsruhe"),
        new CostVertex(6, 0), // "Erfurt"),
        new CostVertex(7, 0), // "Nuernberg"),
        new CostVertex(8, 0), // "Augsburg"),
        new CostVertex(9, 0), // "Nuernberg")
    };

    // frankfurt -> mannheim, kassel, wuerzburg
    graph.addVertex(cities[0], new CostVertex(cities[1].getVertexId(), 85),
        new CostVertex(cities[4].getVertexId(), 173),
        new CostVertex(cities[2].getVertexId(), 217));

    // mannheim -> karlsruhe, frankfurt
    graph.addVertex(cities[1], new CostVertex(cities[0].getVertexId(), 85),
        new CostVertex(cities[5].getVertexId(), 80));

    // wuerzburg -> erfurt, frankfurt, nuernberg
    graph.addVertex(cities[2], new CostVertex(cities[0].getVertexId(), 217),
        new CostVertex(cities[6].getVertexId(), 186),
        new CostVertex(cities[7].getVertexId(), 103));

    // stuttgart -> nuernberg
    graph.addVertex(cities[3], new CostVertex(cities[7].getVertexId(), 183));

    // kassel -> muenchen
    graph.addVertex(cities[4], new CostVertex(cities[9].getVertexId(), 502));

    // karlsruhe -> mannheim, augsburg
    graph.addVertex(cities[5], new CostVertex(cities[8].getVertexId(), 250),
        new CostVertex(cities[1].getVertexId(), 80));

    // erfurt -> wuerzburg
    graph.addVertex(cities[6], new CostVertex(cities[2].getVertexId(), 186));

    // nuernberg -> stuttgart, muenchen, wuerzburg
    graph.addVertex(cities[7], new CostVertex(cities[3].getVertexId(), 183),
        new CostVertex(cities[9].getVertexId(), 167),
        new CostVertex(cities[2].getVertexId(), 103));

    // augsburg -> karlsruhe, muenchen
    graph.addVertex(cities[8], new CostVertex(cities[5].getVertexId(), 250),
        new CostVertex(cities[9].getVertexId(), 84));

    // muenchen -> nuernberg, kassel, augsburg
    graph.addVertex(cities[9], new CostVertex(cities[7].getVertexId(), 167),
        new CostVertex(cities[4].getVertexId(), 173),
        new CostVertex(cities[8].getVertexId(), 84));

    return graph;
  }
}
