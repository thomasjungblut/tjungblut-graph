package de.jungblut.graph.search;

import com.google.common.collect.ImmutableList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.Tuple;
import de.jungblut.graph.model.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TriangleCounter {

    public static <K, V, E> int countTriangles(Graph<K, V, E> graph) {
        int numTriangles = 0;

        HashSet<K> visited = new HashSet<>();
        for (K vId : graph.getVertexIDSet()) {

            if (visited.add(vId)) {
                for (Tuple<Vertex<K, V>, Vertex<K, V>> pair : pairs(graph, vId)) {

                    if (graph.getAdjacentVertices(pair.getFirst()).contains(
                            pair.getSecond())
                            && graph.getAdjacentVertices(pair.getSecond()).contains(
                            pair.getFirst())) {

                        numTriangles++;
                        visited.add(pair.getFirst().getVertexId());
                        visited.add(pair.getSecond().getVertexId());
                    }
                }
            }
        }

        return numTriangles;
    }

    private static <K, V, E> List<Tuple<Vertex<K, V>, Vertex<K, V>>> pairs(
            Graph<K, V, E> graph, K vertexId) {
        List<Tuple<Vertex<K, V>, Vertex<K, V>>> lst = new ArrayList<>();

        List<Vertex<K, V>> adjacents = ImmutableList.copyOf(graph
                .getAdjacentVertices(vertexId));

        for (int i = 0; i < adjacents.size(); i++) {
            for (int j = i + 1; j < adjacents.size(); j++) {
                lst.add(new Tuple<Vertex<K, V>, Vertex<K, V>>(adjacents.get(i),
                        adjacents.get(j)));
            }
        }

        return lst;
    }

}
