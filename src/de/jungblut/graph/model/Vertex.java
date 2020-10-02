package de.jungblut.graph.model;

/**
 * Generic vertex interface that contains methods to get id and value.
 *
 * @param <VERTEX_ID>    the vertex id type.
 * @param <VERTEX_VALUE> the vertex value type.
 * @author thomas.jungblut
 */
public interface Vertex<VERTEX_ID, VERTEX_VALUE> {

    /**
     * @return the id of this vertex.
     */
    public VERTEX_ID getVertexId();

    /**
     * @return the value of this vertex.
     */
    public VERTEX_VALUE getVertexValue();

}
