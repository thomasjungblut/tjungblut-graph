package de.jungblut.graph.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A tuple for id and cost that is comparable to the ascending distance.
 *
 * @author thomas.jungblut
 */
public class IdCostTuple<VERTEX_ID, DISTANCE extends Comparable<DISTANCE>> implements
        Comparable<IdCostTuple<VERTEX_ID, DISTANCE>> {

    private final VERTEX_ID vertexId;
    private final DISTANCE distance;
    private VERTEX_ID source;

    public IdCostTuple(VERTEX_ID vertex, DISTANCE distance) {
        this.vertexId = checkNotNull(vertex);
        this.distance = checkNotNull(distance);
    }

    public IdCostTuple(VERTEX_ID vertex, VERTEX_ID source, DISTANCE distance) {
        this.vertexId = checkNotNull(vertex);
        this.source = checkNotNull(source);
        this.distance = checkNotNull(distance);
    }

    @Override
    public int compareTo(IdCostTuple<VERTEX_ID, DISTANCE> o) {
        return this.distance.compareTo(o.distance);
    }

    @Override
    public String toString() {
        return distance + "";
    }

    public DISTANCE getDistance() {
        return this.distance;
    }

    public VERTEX_ID getVertexId() {
        return this.vertexId;
    }

    public VERTEX_ID getSource() {
        return this.source;
    }
}
