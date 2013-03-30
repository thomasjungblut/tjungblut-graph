package de.jungblut.graph;

import java.awt.Point;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;

public class DenseGraphTest extends TestCase {

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
          assertNotNull(vertex);
          assertNotNull(vertex.getVertexId());
          assertNotNull(vertex.getVertexValue());
          assertEquals(1, vertex.getVertexValue().intValue());
          assertEquals(i, vertex.getVertexId().x);
          assertEquals(j, vertex.getVertexId().y);
        } else {
          assertNotNull(vertex);
          assertNotNull(vertex.getVertexId());
          assertNull(vertex.getVertexValue());
        }
      }
    }

  }
}
