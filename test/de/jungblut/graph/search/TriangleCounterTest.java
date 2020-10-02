package de.jungblut.graph.search;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

public class TriangleCounterTest {

    @Test
    public void testSimpleCase() {
        AdjacencyList<Integer, Integer, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<Integer, Integer>(1, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(2, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(3, 0));

        g.addEdge(1, new Edge<Integer, Integer>(2, 0));
        g.addEdge(1, new Edge<Integer, Integer>(3, 0));

        g.addEdge(2, new Edge<Integer, Integer>(1, 0));
        g.addEdge(2, new Edge<Integer, Integer>(3, 0));

        g.addEdge(3, new Edge<Integer, Integer>(1, 0));
        g.addEdge(3, new Edge<Integer, Integer>(2, 0));

        int triangles = TriangleCounter.countTriangles(g);
        Assert.assertEquals(1, triangles);
    }

    @Test
    public void testBridge() {
        AdjacencyList<Integer, Integer, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<Integer, Integer>(1, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(2, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(3, 0));

        g.addVertex(new VertexImpl<Integer, Integer>(4, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(5, 0));

        g.addEdge(1, new Edge<Integer, Integer>(2, 0));
        g.addEdge(1, new Edge<Integer, Integer>(3, 0));

        g.addEdge(2, new Edge<Integer, Integer>(1, 0));
        g.addEdge(2, new Edge<Integer, Integer>(3, 0));

        g.addEdge(3, new Edge<Integer, Integer>(1, 0));
        g.addEdge(3, new Edge<Integer, Integer>(2, 0));
        g.addEdge(3, new Edge<Integer, Integer>(4, 0));
        g.addEdge(3, new Edge<Integer, Integer>(5, 0));

        g.addEdge(4, new Edge<Integer, Integer>(3, 0));
        g.addEdge(4, new Edge<Integer, Integer>(5, 0));

        g.addEdge(5, new Edge<Integer, Integer>(3, 0));
        g.addEdge(5, new Edge<Integer, Integer>(4, 0));

        int triangles = TriangleCounter.countTriangles(g);
        Assert.assertEquals(2, triangles);
    }

    @Test
    public void testNoTriangle() {
        AdjacencyList<Integer, Integer, Integer> g = new AdjacencyList<>();
        g.addVertex(new VertexImpl<Integer, Integer>(1, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(2, 0));
        g.addVertex(new VertexImpl<Integer, Integer>(3, 0));

        g.addEdge(1, new Edge<Integer, Integer>(2, 0));
        g.addEdge(2, new Edge<Integer, Integer>(3, 0));

        int triangles = TriangleCounter.countTriangles(g);
        Assert.assertEquals(0, triangles);
    }

}
