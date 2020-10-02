package de.jungblut.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class AdjacencyListTest {

    @Test
    public void testGraphRepresentation() {
        Graph<Integer, String, Integer> wikipediaExampleGraph = TestGraphProvider
                .getWikipediaExampleGraph();

        // check if all vertices can be retrieved correctly and the size are correct
        Assert.assertEquals(10, wikipediaExampleGraph.getNumVertices());
        Assert.assertEquals(22, wikipediaExampleGraph.getNumEdges());

        IntStream.range(0, 9).forEach(
                (i) -> Assert.assertNotNull(wikipediaExampleGraph.getVertex(i)));

    }
}
