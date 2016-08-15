package de.jungblut.graph.reader;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.common.base.Optional;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;

/**
 * Reader interface for vertices.
 * 
 * @author thomas.jungblut
 * 
 * @param <VERTEX_ID> the vertex id type.
 * @param <VERTEX_VALUE> the vertex value type.
 * @param <EDGE_VALUE> the edge value type.
 */
public interface VertexReader<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

  /**
   * Reads the graph from a artificial resource.
   * 
   * @param optionalGraph a optional graph that can be passed and will be
   *          filled. If none supplied it will use the {@link AdjacencyList}.
   * @return a new graph or the one passed through the argument.
   * @throws IOException in case of a {@link FileNotFoundException} or some
   *           other IO Error.
   */
  public Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> readGraph(
      Optional<Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE>> optionalGraph)
      throws IOException;

}
