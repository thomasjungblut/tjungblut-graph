package de.jungblut.graph;

import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.Random;

public class DenseGraphTest {

    @Test
    public void testGraphRepresentation() {
        DenseGraph<Integer> g = new DenseGraph<>(10, 10);

        int[][] testRepresentation = new int[10][10];

        Random r = new Random(1L);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // omit 0.4% of our dense grid to mimic "walls"
                if (r.nextDouble() > 0.6) {
                    g.addVertex(new VertexImpl<>(new Point(i, j), 1));
                    testRepresentation[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Vertex<Point, Integer> vertex = g.getVertex(new Point(i, j));
                if (testRepresentation[i][j] == 1) {
                    Assert.assertNotNull(vertex);
                    Assert.assertNotNull(vertex.getVertexId());
                    Assert.assertNotNull(vertex.getVertexValue());
                    Assert.assertEquals(1, vertex.getVertexValue().intValue());
                    Assert.assertEquals(i, vertex.getVertexId().x);
                    Assert.assertEquals(j, vertex.getVertexId().y);
                } else {
                    Assert.assertNotNull(vertex);
                    Assert.assertNotNull(vertex.getVertexId());
                    Assert.assertNull(vertex.getVertexValue());
                }
            }
        }

    }
}
