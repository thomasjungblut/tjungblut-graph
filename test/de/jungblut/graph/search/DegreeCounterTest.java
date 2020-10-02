package de.jungblut.graph.search;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

public class DegreeCounterTest {

    @Test
    public void testDegreeCountsHappyPathWikipediaExampleUndirectedGraph() {
        Graph<Integer, String, Integer> g = TestGraphProvider.getWikipediaExampleGraph();
        Graph<Integer, DegreeCounter<Integer, String, Integer>.DegreeInformation, Integer>
                result = new DegreeCounter<Integer, String, Integer>().computeDegreeGraph(g);

        int[] expectedInAndOutlinks = {3, 2, 3, 1, 2, 2, 1, 3, 2, 3};
        Assert.assertEquals(g.getVertexIDSet(), result.getVertexIDSet());
        for (Integer vertexId : result.getVertexIDSet()) {
            DegreeCounter<Integer, String, Integer>.DegreeInformation di = result.getVertex(vertexId).getVertexValue();
            Assert.assertEquals(vertexId + " has unexpected inlink counts", expectedInAndOutlinks[vertexId], di.getInDegree());
            Assert.assertEquals(vertexId + " has unexpected outlink counts", expectedInAndOutlinks[vertexId], di.getOutDegree());
            Assert.assertEquals(g.getVertex(vertexId).getVertexValue(), di.getOriginalValue());
        }
    }

    @Test
    public void testSimpleUndirectedLoopGraph() {
        // this is basically https://en.wikipedia.org/wiki/Degree_(graph_theory)#/media/File:UndirectedDegrees_(Loop).svg
        // numeration is from left to bottom to top right
        Graph<Integer, String, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<>(0, "0"));
        g.addVertex(new VertexImpl<>(1, "1"),
                new Edge<>(2, 0));
        g.addVertex(new VertexImpl<>(2, "2"),
                new Edge<>(1, 0),
                new Edge<>(3, 0),
                new Edge<>(6, 0)
        );
        g.addVertex(new VertexImpl<>(3, "3"),
                new Edge<>(2, 0),
                new Edge<>(4, 0)
        );
        g.addVertex(new VertexImpl<>(4, "4"),
                new Edge<>(3, 0),
                new Edge<>(4, 0),
                new Edge<>(5, 0),
                new Edge<>(6, 0)
        );
        g.addVertex(new VertexImpl<>(5, "5"),
                new Edge<>(4, 0),
                new Edge<>(6, 0)
        );
        g.addVertex(new VertexImpl<>(6, "6"),
                new Edge<>(2, 0),
                new Edge<>(4, 0),
                new Edge<>(5, 0)
        );

        Graph<Integer, DegreeCounter<Integer, String, Integer>.DegreeInformation, Integer>
                result = new DegreeCounter<Integer, String, Integer>().computeDegreeGraph(g);
        int[] expectedInAndOutlinks = {0, 1, 3, 2, 5, 2, 3};
        Assert.assertEquals(g.getVertexIDSet(), result.getVertexIDSet());
        for (Integer vertexId : result.getVertexIDSet()) {
            DegreeCounter<Integer, String, Integer>.DegreeInformation di = result.getVertex(vertexId).getVertexValue();
            Assert.assertEquals(vertexId + " has unexpected inlink counts", expectedInAndOutlinks[vertexId], di.getInDegree());
            Assert.assertEquals(vertexId + " has unexpected outlink counts", expectedInAndOutlinks[vertexId], di.getOutDegree());
            Assert.assertEquals(g.getVertex(vertexId).getVertexValue(), di.getOriginalValue());
        }
    }

    @Test
    public void testSimpleDirectedGraph() {
        Graph<Integer, String, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<>(0, "0"));
        g.addVertex(new VertexImpl<>(1, "1"),
                new Edge<>(2, 0));
        g.addVertex(new VertexImpl<>(2, "2"),
                new Edge<>(3, 0)
        );
        g.addVertex(new VertexImpl<>(3, "3"),
                new Edge<>(0, 0)
        );

        Graph<Integer, DegreeCounter<Integer, String, Integer>.DegreeInformation, Integer>
                result = new DegreeCounter<Integer, String, Integer>().computeDegreeGraph(g);
        int[] expectedInLinks = {1, 0, 1, 1};
        int[] expectedOutLinks = {0, 1, 1, 1};
        Assert.assertEquals(g.getVertexIDSet(), result.getVertexIDSet());
        for (Integer vertexId : result.getVertexIDSet()) {
            DegreeCounter<Integer, String, Integer>.DegreeInformation di = result.getVertex(vertexId).getVertexValue();
            Assert.assertEquals(vertexId + " has unexpected inlink counts", expectedInLinks[vertexId], di.getInDegree());
            Assert.assertEquals(vertexId + " has unexpected outlink counts", expectedOutLinks[vertexId], di.getOutDegree());
            Assert.assertEquals(g.getVertex(vertexId).getVertexValue(), di.getOriginalValue());
        }
    }

}
