package de.jungblut.graph.sort;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.model.Vertex;

public class TopologicalSortTest extends TestCase {

  @Test
  public void testTopologicalSort() {

    Graph<Integer, String, Integer> g = TestGraphProvider
        .getTopologicalSortWikipediaExampleGraph();
    List<Vertex<Integer, String>> sort = TopologicalSort.sort(g);
    int[] result = new int[] { 7, 5, 11, 3, 10, 8, 9, 2 };
    int index = 0;
    for (Vertex<Integer, String> v : sort) {
      System.out.println(v.getVertexId());
      assertEquals(result[index], v.getVertexId().intValue());
      index++;
    }

  }

}
