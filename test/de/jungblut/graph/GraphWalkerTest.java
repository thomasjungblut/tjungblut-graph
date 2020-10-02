package de.jungblut.graph;

import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class GraphWalkerTest {

    @Test
    public void testBreadthFirstTraversal() throws Exception {

        Graph<Integer, String, Integer> g = TestGraphProvider
                .getWikipediaExampleGraph();
        Iterator<VertexImpl<Integer, String>> it = GraphWalker.breadthFirst(g, g
                .getVertex(0).getVertexId());
        // this actually might have order involved, which is not guranteed by the
        // walker
        int[] iterationIds = new int[]{0, 1, 2, 4, 5, 6, 7, 9, 8, 3};
        int index = 0;
        while (it.hasNext()) {
            VertexImpl<Integer, String> next = it.next();
            Assert.assertEquals(iterationIds[index++], next.getVertexId().intValue());
        }
    }

    @Test
    public void testDepthFirstTraversal() throws Exception {

        Graph<Integer, String, Integer> g = TestGraphProvider
                .getWikipediaExampleGraph();
        Iterator<VertexImpl<Integer, String>> it = GraphWalker.depthFirst(g, g
                .getVertex(0).getVertexId());
        // this actually might have order involved, which is not guranteed by the
        // walker
        int[] iterationIds = new int[]{0, 4, 9, 8, 5, 7, 3, 2, 6, 1};
        int index = 0;
        while (it.hasNext()) {
            VertexImpl<Integer, String> next = it.next();
            Assert.assertEquals(iterationIds[index++], next.getVertexId().intValue());
        }
    }

    @Test
    public void testEdgeTraversal() throws Exception {

        Graph<Integer, String, Integer> g = TestGraphProvider
                .getWikipediaExampleGraph();
        Iterator<Tuple<Integer, Edge<Integer, Integer>>> it = GraphWalker
                .iterateEdges(g, g.getVertexIDSet());
        // this actually might have order involved, which is not guranteed by the
        // walker
        int[] iterationIds = new int[]{1, 2, 4, 5, 0, 6, 7, 0, 7, 9, 0, 1, 8, 2, 9,
                2, 3, 5, 9, 7, 4, 8};
        int index = 0;
        while (it.hasNext()) {
            Tuple<Integer, Edge<Integer, Integer>> next = it.next();
            Assert.assertEquals(iterationIds[index++], next.getSecond()
                    .getDestinationVertexID().intValue());
        }
    }
}
