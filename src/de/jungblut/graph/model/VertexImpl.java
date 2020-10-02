package de.jungblut.graph.model;

/**
 * The concrete Vertex implementation that contains id and value.
 *
 * @param <VERTEX_ID>    the id type of the vertex.
 * @param <VERTEX_VALUE> the value type of the vertex.
 * @author thomas.jungblut
 */
public final class VertexImpl<VERTEX_ID, VERTEX_VALUE> implements
        Vertex<VERTEX_ID, VERTEX_VALUE> {

    private final VERTEX_ID id;
    private final VERTEX_VALUE value;

    public VertexImpl(VERTEX_ID id, VERTEX_VALUE value) {
        super();
        this.id = id;
        this.value = value;
    }

    @Override
    public VERTEX_ID getVertexId() {
        return id;
    }

    @Override
    public VERTEX_VALUE getVertexValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        VertexImpl other = (VertexImpl) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VertexImpl [id=" + this.id + ", value=" + this.value + "]";
    }

}
