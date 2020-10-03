package de.jungblut.graph.partition;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.Tuple;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class StoerWagnerMinCutTest {

    @Test
    public void minCutHappyPathWikipediaExampleGraph() {
        Graph<Integer, String, Integer> g = minCutExampleGraph();
        StoerWagnerMinCut<Integer, String>.MinCut minCut = new StoerWagnerMinCut<Integer, String>().computeMinCut(g);
        Assert.assertNotNull(minCut.getFirst());
        Assert.assertEquals(4, minCut.getFirst().getNumVertices());
        Assert.assertEquals(10, minCut.getFirst().getNumEdges());
        Assert.assertEquals(new HashSet<>(Arrays.asList(3, 4, 7, 8)), minCut.getFirst().getVertexIDSet());
        Assert.assertNotNull(minCut.getSecond());
        Assert.assertEquals(4, minCut.getSecond().getNumVertices());
        Assert.assertEquals(10, minCut.getSecond().getNumEdges());
        Assert.assertEquals(new HashSet<>(Arrays.asList(1, 2, 5, 6)), minCut.getSecond().getVertexIDSet());
        Assert.assertEquals(4, minCut.getMinCutWeight());
        Assert.assertEquals(4, minCut.getEdgesOnTheCut().size());
        // basically both of these edges should go between 2-3 and 6-7 and should sum to the min cut weight
        List<Tuple<Vertex<Integer, String>, Edge<Integer, Integer>>> edgesOnTheCut = minCut.getEdgesOnTheCut();
        assertVertexAndEdgeEquals(edgesOnTheCut.get(0), 2, "2", 3, 3);
        assertVertexAndEdgeEquals(edgesOnTheCut.get(1), 3, "3", 2, 3);
        assertVertexAndEdgeEquals(edgesOnTheCut.get(2), 6, "6", 7, 1);
        assertVertexAndEdgeEquals(edgesOnTheCut.get(3), 7, "7", 6, 1);
        Assert.assertEquals(4, edgesOnTheCut.get(0).getSecond().getValue() + edgesOnTheCut.get(2).getSecond().getValue());
    }

    private void assertVertexAndEdgeEquals(Tuple<Vertex<Integer, String>, Edge<Integer, Integer>> tuple,
                                           int sourceId, String sourceValue, int destId, int edgeWeight) {
        Assert.assertEquals(sourceId, tuple.getFirst().getVertexId().intValue());
        Assert.assertEquals(sourceValue, tuple.getFirst().getVertexValue());
        Assert.assertEquals(destId, tuple.getSecond().getDestinationVertexID().intValue());
        Assert.assertEquals(edgeWeight, tuple.getSecond().getValue().intValue());
    }

    @Test
    public void testMaxAdjacencySearchWithPaperExample() {
        Graph<Integer, String, Integer> g = minCutExampleGraph();
        StoerWagnerMinCut<Integer, String> minCut = new StoerWagnerMinCut<>();
        StoerWagnerMinCut.CutOfThePhase<Integer> cutOfThePhase = minCut.maximumAdjacencySearch(g, 2);
        Assert.assertEquals(1, cutOfThePhase.t.intValue());
        Assert.assertEquals(5, cutOfThePhase.s.intValue());
        Assert.assertEquals(5, cutOfThePhase.weight);
    }

    @Test
    public void testMergingFromCut() {
        Graph<Integer, String, Integer> g = minCutExampleGraph();
        StoerWagnerMinCut<Integer, String> minCut = new StoerWagnerMinCut<>();
        StoerWagnerMinCut.CutOfThePhase<Integer> cutOfThePhase = new StoerWagnerMinCut.CutOfThePhase<>(5, 1, 5);
        Graph<Integer, String, Integer> gPrime = minCut.mergeVerticesFromCut(g, cutOfThePhase);

        Assert.assertEquals(g.getNumVertices() - 1, gPrime.getNumVertices());
        Assert.assertEquals(g.getNumEdges() - 4, gPrime.getNumEdges());
        Assert.assertNull(gPrime.getVertex(1));
        Assert.assertEquals(0, gPrime.getEdges(1).size());

        Assert.assertEquals(4, gPrime.getEdge(5, 2).getValue().intValue());
        Assert.assertEquals(4, gPrime.getEdge(2, 5).getValue().intValue());
        Assert.assertNull(gPrime.getEdge(1, 2));
        Assert.assertNull(gPrime.getEdge(1, 5));

        Assert.assertEquals(g.getEdge(5, 6), gPrime.getEdge(5, 6));
    }

    @Test
    public void testMergingFromCutWithEdgeTransfer() {
        Graph<Integer, String, Integer> g = minCutExampleGraphForEdgeTransfer();
        StoerWagnerMinCut<Integer, String> minCut = new StoerWagnerMinCut<>();
        StoerWagnerMinCut.CutOfThePhase<Integer> cutOfThePhase = new StoerWagnerMinCut.CutOfThePhase<>(3, 7, 6);
        Graph<Integer, String, Integer> gPrime = minCut.mergeVerticesFromCut(g, cutOfThePhase);

        Assert.assertEquals(g.getNumVertices() - 1, gPrime.getNumVertices());
        Assert.assertEquals(g.getNumEdges() - 2, gPrime.getNumEdges());
        Assert.assertNull(gPrime.getVertex(7));
        Assert.assertEquals(0, gPrime.getEdges(7).size());

        Assert.assertEquals(3, gPrime.getEdge(2, 3).getValue().intValue());
        Assert.assertEquals(3, gPrime.getEdge(3, 2).getValue().intValue());
        Assert.assertEquals(1, gPrime.getEdge(3, 6).getValue().intValue());
        Assert.assertEquals(1, gPrime.getEdge(6, 3).getValue().intValue());
    }

    // TODO test the negative edges and numVertices conditions


    // https://fktpm.ru/file/204-stoer-wagner-a-simple-min-cut-algorithm.pdf page 5
    @SuppressWarnings("unchecked")
    private Graph<Integer, String, Integer> minCutExampleGraph() {
        Graph<Integer, String, Integer> g = new AdjacencyList<>();

        g.addVertex(new VertexImpl<>(1, "1"),
                new Edge<>(2, 2),
                new Edge<>(5, 3));
        g.addVertex(new VertexImpl<>(2, "2"),
                new Edge<>(1, 2),
                new Edge<>(3, 3),
                new Edge<>(5, 2),
                new Edge<>(6, 2));
        g.addVertex(new VertexImpl<>(3, "3"),
                new Edge<>(2, 3),
                new Edge<>(4, 4),
                new Edge<>(7, 2));
        g.addVertex(new VertexImpl<>(4, "4"),
                new Edge<>(3, 4),
                new Edge<>(7, 2),
                new Edge<>(8, 2));
        g.addVertex(new VertexImpl<>(5, "5"),
                new Edge<>(1, 3),
                new Edge<>(6, 3),
                new Edge<>(2, 2));
        g.addVertex(new VertexImpl<>(6, "6"),
                new Edge<>(2, 2),
                new Edge<>(5, 3),
                new Edge<>(7, 1));
        g.addVertex(new VertexImpl<>(7, "7"),
                new Edge<>(6, 1),
                new Edge<>(3, 2),
                new Edge<>(4, 2),
                new Edge<>(8, 3));
        g.addVertex(new VertexImpl<>(8, "8"),
                new Edge<>(4, 2),
                new Edge<>(7, 3));
        return g;
    }


    @SuppressWarnings("unchecked")
    private Graph<Integer, String, Integer> minCutExampleGraphForEdgeTransfer() {
        Graph<Integer, String, Integer> g = new AdjacencyList<>();

        g.addVertex(new VertexImpl<>(1, "1"),
                new Edge<>(2, 2),
                new Edge<>(5, 3));
        g.addVertex(new VertexImpl<>(2, "2"),
                new Edge<>(1, 2),
                new Edge<>(3, 3),
                new Edge<>(5, 2),
                new Edge<>(6, 2));
        g.addVertex(new VertexImpl<>(3, "3"),
                new Edge<>(2, 3),
                new Edge<>(7, 2));
        g.addVertex(new VertexImpl<>(5, "5"),
                new Edge<>(1, 3),
                new Edge<>(6, 3),
                new Edge<>(2, 2));
        g.addVertex(new VertexImpl<>(6, "6"),
                new Edge<>(2, 2),
                new Edge<>(5, 3),
                new Edge<>(7, 1));
        g.addVertex(new VertexImpl<>(7, "7"),
                new Edge<>(6, 1),
                new Edge<>(3, 2));
        return g;
    }


}
