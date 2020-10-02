package de.jungblut.graph.bsp;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.bsp.MindistSearch.MindistSearchVertex;
import de.jungblut.graph.bsp.MindistSearch.TabToTextVertexReader;
import de.jungblut.graph.model.Vertex;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public final class MindistSearchTest {

    @Test
    @Ignore("BSP partitioning job fails without proper reason")
    public void testSSSP() throws Exception {

        // Graph job configuration
        HamaConfiguration conf = new HamaConfiguration();
        conf.set("bsp.local.tasks.maximum", "1");
        GraphJob job = new GraphJob(conf, MindistSearch.class);
        FileSystem fs = FileSystem.get(conf);
        Path in = new Path(TestHelpers.getTempDir() + "/mdst/input.txt");
        createInput(fs, in);
        Path out = new Path(TestHelpers.getTempDir() + "/mdst/out/");
        if (fs.exists(out)) {
            fs.delete(out, true);
        }
        job.setInputPath(in);
        job.setOutputPath(out);

        job.setVertexIDClass(Text.class);
        job.setVertexValueClass(Text.class);
        job.setEdgeValueClass(NullWritable.class);
        job.setVertexClass(MindistSearchVertex.class);

        job.setInputKeyClass(LongWritable.class);
        job.setInputValueClass(Text.class);
        job.setInputFormat(TextInputFormat.class);
        job.setVertexInputReaderClass(TabToTextVertexReader.class);
        job.setPartitioner(HashPartitioner.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        long startTime = System.currentTimeMillis();
        if (job.waitForCompletion(true)) {
            System.out.println("Job Finished in "
                    + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
        }
        verifyOutput(fs, out);
    }

    private void createInput(FileSystem fs, Path in) throws IOException {
        if (fs.exists(in)) {
            fs.delete(in, true);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                fs.create(in)))) {

            Graph<Integer, String, Integer> wikipediaExampleGraph = TestGraphProvider
                    .getWikipediaExampleGraph();
            for (Vertex<Integer, String> v : wikipediaExampleGraph.getVertexSet()) {
                Set<Vertex<Integer, String>> adjacentVertices = wikipediaExampleGraph
                        .getAdjacentVertices(v);
                writer.write(v.getVertexId() + "\t" + toString(adjacentVertices));
                writer.write('\n');
            }
        }
    }

    private String toString(Set<Vertex<Integer, String>> adjacentVertices) {
        StringBuilder sb = new StringBuilder();
        for (Vertex<Integer, String> v : adjacentVertices) {
            sb.append(v.getVertexId());
            sb.append('\t');
        }
        return sb.toString();
    }

    private void verifyOutput(FileSystem fs, Path out) throws IOException {
        int[] result = new int[10];
        Arrays.fill(result, 1);
        FileStatus[] status = fs.listStatus(out);
        for (FileStatus fss : status) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fs.open(fss.getPath())))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    String[] split = line.split("\t");
                    result[Integer.parseInt(split[0])] = Integer.parseInt(split[1]);
                }
            }
        }

        // ensure everything is zero, as zero is the smallest vertex in the whole
        // component
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(0, result[i]);
        }

    }
}
