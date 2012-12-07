package de.jungblut.graph.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.VertexImpl;

/**
 * Vertex Reader that parses a line of text into a cost vertex. <br/>
 * The format of the graph file must have the following order: VERTEX_ID /
 * DESTINATION_VERTEX_ID / COST, where "/" is the delimiter which can be
 * configured. <b>Note</b> that each of them must have a numerical value.
 * 
 * @author thomas.jungblut
 */
public final class CostVertexLineReader implements
    VertexReader<Integer, Integer, Integer> {

  private final Pattern splitPattern;
  private final boolean undirected;

  private String path;
  private int skipLines;

  /**
   * Constructs a new reader for tab delimited files that need no skip lines.
   * 
   * @param path the path of the text file.
   * @param undirected true if graph should be undirected.
   */
  public CostVertexLineReader(String path, boolean undirected) {
    this(path, '\t', 0, undirected);
  }

  /**
   * Constructs a new reader.
   * 
   * @param path the path where the text file is stored.
   * @param delimiter the delimiter for the vertices and costs.
   * @param skipLines how many lines to skip
   * @param undirected true if graph should be undirected.
   */
  public CostVertexLineReader(String path, char delimiter, int skipLines,
      boolean undirected) {
    this.path = path;
    this.skipLines = skipLines;
    this.undirected = undirected;
    this.splitPattern = Pattern.compile("" + delimiter);
  }

  @Override
  public Graph<Integer, Integer, Integer> readGraph(
      Optional<Graph<Integer, Integer, Integer>> optionalGraph)
      throws IOException {
    // check if graph is not absent
    Graph<Integer, Integer, Integer> graph;
    if (optionalGraph.isPresent()) {
      graph = optionalGraph.get();
    } else {
      graph = new AdjacencyList<>();
    }

    // read line by line
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line = null;
      int lineNumber = 1;
      while ((line = br.readLine()) != null) {
        // only parse if we are further ahead than skipLines
        if (lineNumber > skipLines) {
          parseLine(graph, line);
        }
        lineNumber++;
      }
    }

    return graph;
  }

  /**
   * Parses a line and constructs a new vertex that is filled and put into the
   * graph.
   */
  private void parseLine(Graph<Integer, Integer, Integer> graph, String line) {
    String[] split = splitPattern.split(line);
    int id = Integer.parseInt(split[0]);
    int dest = Integer.parseInt(split[1]);
    int cost = Integer.parseInt(split[2]);
    VertexImpl<Integer, Integer> vertex = new VertexImpl<Integer, Integer>(id,
        null);
    graph.addVertex(vertex, new Edge<Integer, Integer>(dest, cost));
    // take care that the destination is there
    graph.addVertex(new VertexImpl<Integer, Integer>(dest, null));
    if (undirected) {
      graph.addEdge(dest,
          new Edge<Integer, Integer>(vertex.getVertexId(), cost));
    }
  }
}
