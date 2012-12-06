package de.jungblut.graph.reader;

import java.io.IOException;
import java.util.Set;

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

    Set<CostVertex> vertexSet = graph.getVertexSet();
    assertEquals(499, vertexSet.size());

    int edgeSum = 0;
    for (CostVertex v : vertexSet) {
      edgeSum += graph.getAdjacentVertices(v).size();
    }

    assertEquals(2175, edgeSum);

  }
}
