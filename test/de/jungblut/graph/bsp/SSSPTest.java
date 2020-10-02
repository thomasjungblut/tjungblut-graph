package de.jungblut.graph.bsp;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.bsp.SSSP.IntIntPairWritable;
import de.jungblut.graph.bsp.SSSP.SSSPTextReader;
import de.jungblut.graph.bsp.SSSP.ShortestPathVertex;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
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
import java.util.EnumSet;
import java.util.Set;

public final class SSSPTest {

    @Test
    @Ignore("BSP partitioning job fails without proper reason")
    public void testSSSP() throws Exception {

        // Graph job configuration
        HamaConfiguration conf = new HamaConfiguration();
        conf.set("bsp.local.tasks.maximum", "2");
        GraphJob ssspJob = new GraphJob(conf, SSSP.class);
        // Set the job name
        ssspJob.setJobName("Single Source Shortest Path");
        FileContext fs = FileContext.getFileContext(conf);
        Path in = new Path(TestHelpers.getTempDir() + "/sssp/input.txt");
        createInput(fs, in);
        Path out = new Path(TestHelpers.getTempDir() + "/sssp/out/");
        if (fs.util().exists(out)) {
            fs.delete(out, true);
        }
        conf.set(SSSP.START_VERTEX, "0");
        ssspJob.setNumBspTask(2);
        ssspJob.setInputPath(in);
        ssspJob.setOutputPath(out);

        ssspJob.setVertexClass(ShortestPathVertex.class);
        ssspJob.setInputFormat(TextInputFormat.class);
        ssspJob.setInputKeyClass(LongWritable.class);
        ssspJob.setInputValueClass(Text.class);

        ssspJob.setPartitioner(HashPartitioner.class);
        ssspJob.setOutputFormat(TextOutputFormat.class);
        ssspJob.setVertexInputReaderClass(SSSPTextReader.class);
        ssspJob.setOutputKeyClass(IntWritable.class);
        ssspJob.setOutputValueClass(IntIntPairWritable.class);
        // Iterate until all the nodes have been reached.
        ssspJob.setMaxIteration(Integer.MAX_VALUE);

        ssspJob.setVertexIDClass(IntWritable.class);
        ssspJob.setVertexValueClass(IntIntPairWritable.class);
        ssspJob.setEdgeValueClass(IntWritable.class);

        long startTime = System.currentTimeMillis();
        if (ssspJob.waitForCompletion(true)) {
            System.out.println("Job Finished in "
                    + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
            verifyOutput(fs, out);
        }
    }

    private void createInput(FileContext fs, Path in) throws IOException {

        if (fs.util().exists(in)) {
            fs.delete(in, true);
        } else {
            fs.mkdir(in.getParent(), FsPermission.getDefault(), true);
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                fs.create(in, EnumSet.of(CreateFlag.CREATE, CreateFlag.OVERWRITE))))) {

            Graph<Integer, String, Integer> wikipediaExampleGraph = TestGraphProvider
                    .getWikipediaExampleGraph();
            for (Vertex<Integer, String> v : wikipediaExampleGraph.getVertexSet()) {
                Set<Edge<Integer, Integer>> adjacentVertices = wikipediaExampleGraph
                        .getEdges(v.getVertexId());
                writer.write(v.getVertexId() + "\t" + toString(adjacentVertices));
                writer.write('\n');
            }
        }
    }

    private String toString(Set<Edge<Integer, Integer>> adjacentVertices) {
        StringBuilder sb = new StringBuilder();
        for (Edge<Integer, Integer> v : adjacentVertices) {
            sb.append(v.getDestinationVertexID());
            sb.append(':');
            sb.append(v.getValue());
            sb.append('\t');
        }
        return sb.toString();
    }

    private void verifyOutput(FileContext fs, Path out) throws IOException {
        int[] costResult = new int[10];
        int[] ancestorResult = new int[10];
        int[] costs = new int[]{0, 85, 217, 503, 173, 165, 403, 320, 415, 487};
        int[] ancestors = new int[]{0, 0, 0, 7, 0, 1, 2, 2, 5, 7};
        RemoteIterator<FileStatus> listStatus = fs.listStatus(out);
        while (listStatus.hasNext()) {
            FileStatus fss = listStatus.next();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fs.open(fss.getPath())))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    String[] split = line.split("\t");
                    costResult[Integer.parseInt(split[0])] = Integer.parseInt(split[2]);
                    ancestorResult[Integer.parseInt(split[0])] = Integer
                            .parseInt(split[1]);
                }
            }
        }

        for (int i = 0; i < ancestorResult.length; i++) {
            Assert.assertEquals(costs[i], costResult[i]);
            Assert.assertEquals(ancestors[i], ancestorResult[i]);
        }

    }
}
