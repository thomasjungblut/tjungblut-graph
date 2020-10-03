package de.jungblut.graph;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AdjacencyListTest {

    @Test
    public void testGraphRepresentation() {
        Graph<Integer, String, Integer> wikipediaExampleGraph = TestGraphProvider
                .getWikipediaExampleGraph();

        // check if all vertices can be retrieved correctly and the size are correct
        Assert.assertEquals(10, wikipediaExampleGraph.getNumVertices());
        Assert.assertEquals(22, wikipediaExampleGraph.getNumEdges());

        for (int i = 0; i < 9; i++) {
            Vertex<Integer, String> vertex = wikipediaExampleGraph.getVertex(i);
            Assert.assertNotNull(vertex);
            Assert.assertEquals(i, vertex.getVertexId().intValue());
        }
    }

    @Test
    public void testGetVertexSet() {
        Graph<Integer, String, Integer> g = TestGraphProvider
                .getWikipediaExampleGraph();
        Set<Integer> vertexIDSet = g.getVertexIDSet();
        Assert.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)), vertexIDSet);
    }

    @Test
    public void testTranspose() {
        Graph<Integer, String, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<>(1, "a"));
        g.addVertex(new VertexImpl<>(2, "b"));
        g.addEdge(1, new Edge<>(2, 5));

        Graph<Integer, String, Integer> transposed = g.transpose();
        Assert.assertEquals(2, transposed.getNumVertices());
        Assert.assertEquals(1, transposed.getNumEdges());

        Assert.assertEquals("a", transposed.getVertex(1).getVertexValue());
        Assert.assertEquals("b", transposed.getVertex(2).getVertexValue());
        Assert.assertNull(transposed.getEdge(1, 2));
        Assert.assertNotNull(transposed.getEdge(2, 1));
        Assert.assertEquals(5, transposed.getEdge(2, 1).getValue().intValue());
    }


}
