package de.jungblut.graph.search;

import com.google.common.collect.HashMultiset;
import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;

public class DegreeCounter<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> {

    public class DegreeInformation {
        private final VERTEX_VALUE originalValue;
        private int inDegree;
        private int outDegree;

        public DegreeInformation(VERTEX_VALUE originalValue) {
            this.originalValue = originalValue;
        }

        public int getInDegree() {
            return inDegree;
        }

        public int getOutDegree() {
            return outDegree;
        }

        public VERTEX_VALUE getOriginalValue() {
            return originalValue;
        }

        @Override
        public String toString() {
            return "DegreeInformation{" +
                    "originalValue=" + originalValue +
                    ", inDegree=" + inDegree +
                    ", outDegree=" + outDegree +
                    '}';
        }
    }


    /**
     * Computes a new graph (which only contains vertices) that has additional @{@link DegreeInformation} attached to each vertex.
     *
     * @param g a graph to derive degree information from
     * @return a new @{@link AdjacencyList} that contains only vertices and their @{@link DegreeInformation} as values.
     */
    public Graph<VERTEX_ID, DegreeInformation, EDGE_VALUE> computeDegreeGraph(Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g) {
        // we do one pass for the outdegree and then one consolidation round for the indegree
        Graph<VERTEX_ID, DegreeInformation, EDGE_VALUE> toReturn = new AdjacencyList<>();

        HashMultiset<VERTEX_ID> inDegreeCounter = HashMultiset.create();
        for (VERTEX_ID vertexId : g.getVertexIDSet()) {
            Vertex<VERTEX_ID, DegreeInformation> vPrime = new VertexImpl<>(vertexId, new DegreeInformation(g.getVertex(vertexId).getVertexValue()));
            for (Edge<VERTEX_ID, EDGE_VALUE> e : g.getEdges(vertexId)) {
                vPrime.getVertexValue().outDegree++;
                inDegreeCounter.add(e.getDestinationVertexID());

                // loops count twice in degrees
                if (e.getDestinationVertexID() == vertexId) {
                    vPrime.getVertexValue().outDegree++;
                    inDegreeCounter.add(e.getDestinationVertexID());
                }
            }
            toReturn.addVertex(vPrime);
        }

        for (VERTEX_ID vertexId : g.getVertexIDSet()) {
            toReturn.getVertex(vertexId).getVertexValue().inDegree = inDegreeCounter.count(vertexId);
        }

        return toReturn;
    }


}
