package de.jungblut.graph.model;

/**
 * Edge class that represents the destination vertex id and the value associated
 * with that edge.
 *
 * @param <VERTEX_ID>  the destination vertex id type.
 * @param <EDGE_VALUE> the edge value type.
 * @author thomas.jungblut
 */
public final class Edge<VERTEX_ID, EDGE_VALUE> {

    private final VERTEX_ID destinationVertexID;
    private final EDGE_VALUE cost;

    public Edge(VERTEX_ID destinationVertexID, EDGE_VALUE cost) {
        this.destinationVertexID = destinationVertexID;
        this.cost = cost;
    }

    public VERTEX_ID getDestinationVertexID() {
        return destinationVertexID;
    }

    public EDGE_VALUE getValue() {
        return cost;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.destinationVertexID == null) ? 0 : this.destinationVertexID
                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        Edge other = (Edge) obj;
        if (this.destinationVertexID == null) {
            if (other.destinationVertexID != null)
                return false;
        } else if (!this.destinationVertexID.equals(other.destinationVertexID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.destinationVertexID + ":" + this.getValue();
    }

}
