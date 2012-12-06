package de.jungblut.graph.reader;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.common.base.Optional;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.vertex.Vertex;

/**
 * Reader interface for vertices.
 * 
 * @author thomas.jungblut
 * 
 * @param <V> the type of the vertex to read.
 */
public interface VertexReader<V extends Vertex> {

  /**
   * Reads the graph from a artificial resource.
   * 
   * @param optionalGraph a optional graph that can be passed and will be
   *          filled. If none supplied it will use the {@link AdjacencyList}.
   * @return a new graph or the one passed through the argument.
   * @throws IOException in case of a {@link FileNotFoundException} or some
   *           other IO Error.
   */
  public Graph<V> readGraph(Optional<Graph<V>> optionalGraph)
      throws IOException;

}
