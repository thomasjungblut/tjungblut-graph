package de.jungblut.graph;

import com.google.common.collect.AbstractIterator;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;

import java.util.*;

/**
 * Graph walker utility classes for most common walks like DFS and BFS.
 *
 * @author thomas.jungblut
 */
public final class GraphWalker {

    /**
     * Returns a iterator over edges in a graph. The order is not preserved.
     *
     * @param graph    the graph.
     * @param vertices the vertices in that graph. (might only a subgraph).
     * @return an iterator that cycles through all the edges of the given
     * vertices.
     */
    public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> Iterator<Tuple<VERTEX_ID, Edge<VERTEX_ID, EDGE_VALUE>>> iterateEdges(
            Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> graph, Set<VERTEX_ID> vertices) {
        return new EdgeIterator<>(graph, vertices);
    }

    /**
     * Returns a depth first search iterator on this graph from the given start
     * vertex.
     *
     * @param <VERTEX_ID>    the vertex id type.
     * @param <VERTEX_VALUE> the vertex value type.
     * @param <EDGE_VALUE>   the edge value type.
     */
    public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>> Iterator<T> depthFirst(
            Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
        return new VertexIterator<>(true, g, start);
    }

    /**
     * Returns a breadth first search iterator on this graph from the given start
     * vertex.
     *
     * @param <VERTEX_ID>    the vertex id type.
     * @param <VERTEX_VALUE> the vertex value type.
     * @param <EDGE_VALUE>   the edge value type.
     */
    public static <VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>> Iterator<T> breadthFirst(
            Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
        return new VertexIterator<>(false, g, start);
    }

    /**
     * Vertex iterator class that is multifunction and can traverse depth and
     * breadth first be using two different methods of the {@link Deque}
     * interface.
     */
    private static final class VertexIterator<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE, T extends Vertex<VERTEX_ID, VERTEX_VALUE>>
            extends AbstractIterator<T> {

        private final boolean depthFirst;
        private final Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g;

        private final Deque<VERTEX_ID> vertexDeque = new LinkedList<>();
        private final HashSet<VERTEX_ID> visitedVertices = new HashSet<>();

        public VertexIterator(boolean depthFirst,
                              Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> g, VERTEX_ID start) {
            this.depthFirst = depthFirst;
            this.g = g;
            visitedVertices.add(start);
            vertexDeque.add(start);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected final T computeNext() {
            if (!vertexDeque.isEmpty()) {
                VERTEX_ID currentVisit = null;
                // in DFS we will remove the first item on the "stack"
                if (depthFirst) {
                    currentVisit = vertexDeque.pop();
                } else {
                    // in BFS we will remove the first from the "queue"
                    currentVisit = vertexDeque.poll();
                }
                // expand our known universe to the adjacent vertices
                for (Vertex<VERTEX_ID, VERTEX_VALUE> v : g
                        .getAdjacentVertices(currentVisit)) {
                    if (!visitedVertices.contains(v.getVertexId())) {
                        if (depthFirst) {
                            // in DFS we will add to the head
                            vertexDeque.push(v.getVertexId());
                        } else {
                            // in DFS we will add to the tail
                            vertexDeque.add(v.getVertexId());
                        }
                        visitedVertices.add(v.getVertexId());
                    }
                }
                return (T) g.getVertex(currentVisit);
            } else {
                return endOfData();
            }
        }
    }

    /**
     * Iterator over edges, uses the list of all vertices to loop over their
     * edges.
     */
    private static final class EdgeIterator<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE>
            extends AbstractIterator<Tuple<VERTEX_ID, Edge<VERTEX_ID, EDGE_VALUE>>> {

        private Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> graph;
        private Iterator<VERTEX_ID> iterator;
        private Deque<Edge<VERTEX_ID, EDGE_VALUE>> currentEdges = new ArrayDeque<>();
        private VERTEX_ID current;

        public EdgeIterator(Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> graph,
                            Set<VERTEX_ID> vertices) {
            this.graph = graph;
            this.iterator = vertices.iterator();
        }

        @Override
        protected Tuple<VERTEX_ID, Edge<VERTEX_ID, EDGE_VALUE>> computeNext() {
            if (currentEdges.isEmpty()) {
                if (iterator.hasNext()) {
                    this.current = iterator.next();
                    currentEdges.addAll(graph.getEdges(current));
                    return new Tuple<>(current, currentEdges.pop());
                } else {
                    return endOfData();
                }
            } else {
                return new Tuple<>(current, currentEdges.pop());
            }
        }

    }

}
