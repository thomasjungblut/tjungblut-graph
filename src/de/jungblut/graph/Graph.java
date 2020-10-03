package de.jungblut.graph;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

import java.util.Set;

/**
 * Basic graph interface, for example concrete subclasses can be sparse
 * adjacency lists, dense matrices or rdf storages as well as graph databases.
 *
 * @param <VERTEX_ID>    the vertex id type.
 * @param <VERTEX_VALUE> the vertex value type.
 * @param <EDGE_VALUE>   the edge value type.
 * @author thomas.jungblut
 */
public interface Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

    /**
     * Adds a vertex with a single adjacent to it.
     */
    void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
                   Edge<VERTEX_ID, EDGE_VALUE> adjacent);

    /**
     * Adds a vertex with a no adjacents to it.
     */
    void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex);

    /**
     * Adds a vertex with a list of adjacents to it.
     */
    void addVertex(Vertex<VERTEX_ID, VERTEX_VALUE> vertex,
                   @SuppressWarnings("unchecked") Edge<VERTEX_ID, EDGE_VALUE>... adjacents);

    /**
     * Gets a set of adjacent vertices from a given vertex id.
     */
    Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(VERTEX_ID vertexId);

    /**
     * Get the vertex by its id.
     */
    Vertex<VERTEX_ID, VERTEX_VALUE> getVertex(VERTEX_ID vertexId);

    /**
     * Gets a set of adjacent vertices from a given vertex.
     */
    Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getAdjacentVertices(Vertex<VERTEX_ID, VERTEX_VALUE> vertex);

    /**
     * Gets all vertices associated with this graph.
     */
    Set<Vertex<VERTEX_ID, VERTEX_VALUE>> getVertexSet();

    /**
     * Gets all verte IDs associated with this graph.
     */
    Set<VERTEX_ID> getVertexIDSet();

    /**
     * Adds an edge to a vertex.
     */
    void addEdge(VERTEX_ID vertexId, Edge<VERTEX_ID, EDGE_VALUE> edge);

    /**
     * Get's a set of edges outbound from the given vertex id.
     *
     * @param vertexId the id of the vertex to get out edges.
     * @return a set of edges.
     */
    Set<Edge<VERTEX_ID, EDGE_VALUE>> getEdges(VERTEX_ID vertexId);


    /**
     * Finds an edge between the source and destination vertex.
     *
     * @return null if nothing found, else the edge between those twos.
     */
    Edge<VERTEX_ID, EDGE_VALUE> getEdge(VERTEX_ID source, VERTEX_ID dest);

    /**
     * @return how many vertices are present in this graph.
     */
    int getNumVertices();

    /**
     * @return how many edges are present in this graph.
     */
    int getNumEdges();

    /**
     * @return the transpose of this graph, meaning that it will reverse all edges.
     * So if there was an edge from A->B, the returned graph will contain one for B->A instead.
     */
    Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> transpose();

}
