package de.jungblut.graph.sort;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TopologicalSortTest {

    @Test
    public void testTopologicalSort() {

        Graph<Integer, String, Integer> g = TestGraphProvider
                .getTopologicalSortWikipediaExampleGraph();
        List<Vertex<Integer, String>> sort = new TopologicalSort().sort(g);
        int[] result = new int[]{7, 5, 11, 3, 10, 8, 9, 2};
        int index = 0;
        for (Vertex<Integer, String> v : sort) {
            Assert.assertEquals(result[index], v.getVertexId().intValue());
            index++;
        }
    }

    @Test
    public void testTopologicalSortFindsCycle() {
        Graph<Integer, String, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<>(1, "a"));
        g.addVertex(new VertexImpl<>(2, "b"));
        g.addEdge(1, new Edge<>(2, 5));
        g.addEdge(2, new Edge<>(1, 5));

        List<Vertex<Integer, String>> sort = new TopologicalSort().sort(g);
        Assert.assertNull(sort);
    }

}
