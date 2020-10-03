package de.jungblut.graph.reader;

import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

public class CostVertexLineReaderTest {

    @Test
    public void testReader() throws IOException {
        CostVertexLineReader reader = new CostVertexLineReader(
                "res/weighted_graph/edges.txt", ' ', 1, true);
        Graph<Integer, Integer, Integer> graph = reader.readGraph(Optional.empty());

        Vertex<Integer, Integer> vertex = graph.getVertex(1);
        Assert.assertNotNull(vertex);

        Assert.assertEquals(500, graph.getNumVertices());

        Assert.assertEquals(2184 * 2, graph.getNumEdges());

    }
}
