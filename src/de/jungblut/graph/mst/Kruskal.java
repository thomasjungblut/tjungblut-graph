package de.jungblut.graph.mst;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.Tuple;
import de.jungblut.graph.model.Edge;

import java.util.*;

public class Kruskal<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE extends Comparable<EDGE_VALUE>> {


    /**
     * Construct a minimum spanning tree with Kruskals algorithm enhanced with a union-find datastructure.
     *
     * @param graph the graph to calculate a MST from.
     * @return a new {@link AdjacencyList} that contains the minimum spanning
     * tree.
     */
    @SuppressWarnings("unchecked")
    public Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> constructMinimumSpanningTree(Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> graph) {
        Graph<VERTEX_ID, VERTEX_VALUE, EDGE_VALUE> mst = new AdjacencyList<>();
        UnionFind<VERTEX_ID> unionFind = new UnionFind<>();
        List<Tuple<VERTEX_ID, Edge<VERTEX_ID, EDGE_VALUE>>> edges = new ArrayList<>();
        Map<VERTEX_ID, UnionFind.Node> nodeMap = new HashMap<>();

        for (VERTEX_ID v : graph.getVertexIDSet()) {
            UnionFind<VERTEX_ID>.Node node = unionFind.addDisjointedVertex(v);
            nodeMap.put(v, node);
            for (Edge<VERTEX_ID, EDGE_VALUE> e : graph.getEdges(v)) {
                edges.add(new Tuple<>(v, e));
            }
        }

        edges.sort(Comparator.comparing(l -> l.getSecond().getValue()));

        for (var edge : edges) {
            VERTEX_ID left = edge.getFirst();
            UnionFind<VERTEX_ID>.Node leftRoot = unionFind.find(nodeMap.get(left));
            VERTEX_ID right = edge.getSecond().getDestinationVertexID();
            UnionFind<VERTEX_ID>.Node rightRoot = unionFind.find(nodeMap.get(right));
            if (leftRoot != rightRoot) {
                mst.addVertex(graph.getVertex(left));
                mst.addVertex(graph.getVertex(right));
                mst.addEdge(left, edge.getSecond());
                unionFind.union(leftRoot, rightRoot);
            }
        }

        return mst;
    }
}
