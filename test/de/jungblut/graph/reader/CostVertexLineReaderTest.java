package de.jungblut.graph.reader;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import com.google.common.base.Optional;

import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.CostVertex;

public class CostVertexLineReaderTest extends TestCase {

  @Test
  public void testReader() throws IOException {
    VertexReader<CostVertex> reader = new CostVertexLineReader(
        "res/weighted_graph/edges.txt", ' ', 1);
    Optional<Graph<CostVertex>> absent = Optional.absent();
    Graph<CostVertex> graph = reader.readGraph(absent);

    CostVertex vertex = graph.getVertex(1);
    assertNotNull(vertex);

    assertEquals(499, graph.getNumVertices());

    assertEquals(2184, graph.getNumEdges());

  }
}
