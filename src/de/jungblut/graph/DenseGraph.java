package de.jungblut.graph;

import com.google.common.collect.Sets;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Simple dense graph model for games that have a board that can be layed out
 * like an array. Coordinates (vertex IDs) are modeled as {@link Point}, Edge
 * costs are constant 1.
 *
 * @param <VERTEX_VALUE> the value at a given point.
 * @author thomas.jungblut
 */
public final class DenseGraph<VERTEX_VALUE> implements
        Graph<Point, VERTEX_VALUE, Integer> {

    private static final Object TAKEN = new Object();

    private final VERTEX_VALUE[][] graph;
    private final int height;
    private final int width;

    private int vertices;
    private int edges;

    @SuppressWarnings("unchecked")
    public DenseGraph(int height, int width) {
        this.height = height;
        this.width = width;
        this.graph = (VERTEX_VALUE[][]) new Object[height][width];
    }

    @Override
    public int getNumVertices() {
        return vertices;
    }

    @Override
    public int getNumEdges() {
        return edges;
    }

    @Override
    public void addVertex(Vertex<Point, VERTEX_VALUE> vertex,
                          List<Edge<Point, Integer>> adjacents) {
        addVertex(vertex);
        for (Edge<Point, Integer> edge : adjacents) {
            addEdge(vertex.getVertexId(), edge);
        }
    }

    @Override
    public void addVertex(Vertex<Point, VERTEX_VALUE> vertex,
                          @SuppressWarnings("unchecked") Edge<Point, Integer>... adjacents) {
        addVertex(vertex);
        for (Edge<Point, Integer> edge : adjacents) {
            addEdge(vertex.getVertexId(), edge);
        }
    }

    @Override
    public void addVertex(Vertex<Point, VERTEX_VALUE> vertex,
                          Edge<Point, Integer> adjacent) {
        addVertex(vertex);
        addEdge(vertex.getVertexId(), adjacent);
    }

    @Override
    public void addVertex(Vertex<Point, VERTEX_VALUE> vertex) {
        graph[vertex.getVertexId().x][vertex.getVertexId().y] = vertex
                .getVertexValue();
        vertices++;
    }

    @Override
    public Set<Vertex<Point, VERTEX_VALUE>> getAdjacentVertices(
            Vertex<Point, VERTEX_VALUE> vertex) {
        return getAdjacentVertices(vertex.getVertexId());
    }

    @Override
    public Set<Vertex<Point, VERTEX_VALUE>> getAdjacentVertices(Point vertexId) {
        Set<Vertex<Point, VERTEX_VALUE>> set = Sets.newHashSet();
        // we just have four directions to check in this graph, also check if null
        int x = vertexId.x;
        int y = vertexId.y;
        for (int i = 0; i < 4; i++) {

            switch (i) {
                case 0:
                    x++;
                    break;
                case 1:
                    y--;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }

            // if we're still in bounds and the value isn't null
            if (!(x < 0 || x >= this.height || y < 0 || y >= this.width)
                    && graph[x][y] != null) {
                set.add(new VertexImpl<>(new Point(x, y), graph[x][y]));
            }
            x = vertexId.x;
            y = vertexId.y;
        }

        return set;
    }

    @Override
    public Vertex<Point, VERTEX_VALUE> getVertex(Point vertexId) {
        return new VertexImpl<>(vertexId, graph[vertexId.x][vertexId.y]);
    }

    @Override
    public Set<Vertex<Point, VERTEX_VALUE>> getVertexSet() {
        Set<Vertex<Point, VERTEX_VALUE>> set = Sets.newHashSet();
        Set<Point> vertexIDSet = getVertexIDSet();
        for (Point p : vertexIDSet) {
            set.add(new VertexImpl<>(p, graph[p.x][p.y]));
        }
        return set;
    }

    @Override
    public Set<Point> getVertexIDSet() {
        Set<Point> set = Sets.newHashSet();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (graph[x][y] != null) {
                    set.add(new Point(x, y));
                }
            }
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addEdge(Point vertexId, Edge<Point, Integer> edge) {
        // just allocate the destination with something, so it is interally not null
        graph[edge.getDestinationVertexID().x][edge.getDestinationVertexID().y] = (VERTEX_VALUE) TAKEN;
        edges++;
    }

    @Override
    public Set<Edge<Point, Integer>> getEdges(Point vertexId) {
        Set<Edge<Point, Integer>> set = Sets.newHashSet();
        Set<Vertex<Point, VERTEX_VALUE>> adjacentVertices = getAdjacentVertices(vertexId);
        for (Vertex<Point, VERTEX_VALUE> v : adjacentVertices) {
            set.add(new Edge<>(v.getVertexId(), 1));
        }
        return set;
    }

    @Override
    public Set<Edge<Point, Integer>> getEdges(Vertex<Point, VERTEX_VALUE> vertex) {
        return getEdges(vertex.getVertexId());
    }

    @Override
    public Edge<Point, Integer> getEdge(Point source, Point dest) {
        return new Edge<>(dest, 1);
    }

}
