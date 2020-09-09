package de.jungblut.graph.mst;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class KruskalTest {

    @Test
    public void testCalculateMST() {

        Graph<Integer, String, Integer> g = TestGraphProvider
                .getWikipediaExampleGraph();

        Kruskal<Integer, String, Integer> kruskal = new Kruskal<>();
        Graph<Integer, String, Integer> mst = kruskal.constructMinimumSpanningTree(g);

        Set<Vertex<Integer, String>> vertexSet = mst.getVertexSet();
        // all vertices must be contained
        Assert.assertEquals(g.getNumVertices(), vertexSet.size());
        // in the wikipedia graph we can only have 9 edges to calculate the MST
        Assert.assertEquals(9, mst.getNumEdges());

        // sum the edge weights to get the total cost of the MST
        int cost = 0;
        for (Vertex<Integer, String> v : vertexSet) {
            Set<Edge<Integer, Integer>> edges = mst.getEdges(v.getVertexId());
            for (Edge<Integer, Integer> e : edges) {
                cost += e.getValue();
            }
        }

        Assert.assertEquals(1278, cost);

    }
}
