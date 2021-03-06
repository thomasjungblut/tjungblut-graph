package de.jungblut.graph.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class MindistSearchTest {

    @Test
    public void testMindistSearch() throws Exception {
        MindistSearchJob.main(new String[]{"res/minsearch/input.txt"});
        // this input should take 3 iterations
        Path pref = new Path("files/min-search/depth_final/part-r-00000");
        Configuration conf = new Configuration();
        HashMap<Long, Long> result = new HashMap<>();
        try (SequenceFile.Reader reader = new Reader(conf, Reader.file(pref))) {
            LongWritable key = new LongWritable();
            VertexWritable val = new VertexWritable();
            while (reader.next(key, val)) {
                System.out.println(key + " | " + val.getVertexId());
                result.put(key.get(), val.getVertexId().get());
            }
        }
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(1l, result.get(1l).longValue());
        Assert.assertEquals(1l, result.get(4l).longValue());
        Assert.assertEquals(1l, result.get(5l).longValue());
        Assert.assertEquals(1l, result.get(7l).longValue());

    }

}
