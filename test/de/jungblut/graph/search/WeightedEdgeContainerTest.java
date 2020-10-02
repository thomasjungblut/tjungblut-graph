package de.jungblut.graph.search;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class WeightedEdgeContainerTest {

    @Test
    public void testReconstruction() {
        HashMap<String, Integer> costs = Maps.newHashMap();
        HashMap<String, String> ancestors = Maps.newHashMap();
        ancestors.put("end0", "end1");
        ancestors.put("end1", "end2");
        ancestors.put("end2", "end3");

        WeightedEdgeContainer<String> container = new WeightedEdgeContainer<>(
                costs, ancestors);
        List<String> path = container.reconstructPath("end0");
        Assert.assertEquals(3, path.size());
        // note that the end vertex is not in the path here
        for (int i = 1; i < 4; i++) {
            Assert.assertEquals("end" + i, path.get(i - 1));
        }
    }

}
