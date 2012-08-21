package de.jungblut.graph.bsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;
import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.bsp.SSSP.SSSPTextReader;
import de.jungblut.graph.bsp.SSSP.ShortestPathVertex;
import de.jungblut.graph.vertex.CostVertex;

public final class SSSPTest extends TestCase {

  @Test
  public void testSSSP() throws Exception {

    // Graph job configuration
    HamaConfiguration conf = new HamaConfiguration();
    conf.set("bsp.local.tasks.maximum", "2");
    GraphJob ssspJob = new GraphJob(conf, SSSP.class);
    // Set the job name
    ssspJob.setJobName("Single Source Shortest Path");
    FileSystem fs = FileSystem.get(conf);
    Path in = new Path("/tmp/sssp/input.txt");
    createInput(fs, in);
    Path out = new Path("/tmp/sssp/out/");
    if (fs.exists(out)) {
      fs.delete(out, true);
    }
    conf.set(SSSP.START_VERTEX, "0");
    ssspJob.setInputPath(in);
    ssspJob.setOutputPath(out);

    ssspJob.setVertexClass(ShortestPathVertex.class);
    ssspJob.setInputFormat(TextInputFormat.class);
    ssspJob.setInputKeyClass(LongWritable.class);
    ssspJob.setInputValueClass(Text.class);

    ssspJob.setPartitioner(HashPartitioner.class);
    ssspJob.setOutputFormat(TextOutputFormat.class);
    ssspJob.setVertexInputReaderClass(SSSPTextReader.class);
    ssspJob.setOutputKeyClass(Text.class);
    ssspJob.setOutputValueClass(IntWritable.class);
    // Iterate until all the nodes have been reached.
    ssspJob.setMaxIteration(Integer.MAX_VALUE);

    ssspJob.setVertexIDClass(Text.class);
    ssspJob.setVertexValueClass(IntWritable.class);
    ssspJob.setEdgeValueClass(IntWritable.class);

    long startTime = System.currentTimeMillis();
    if (ssspJob.waitForCompletion(true)) {
      System.out.println("Job Finished in "
          + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
      verifyOutput(fs, out);
    }
  }

  private void createInput(FileSystem fs, Path in) throws IOException {
    if (fs.exists(in)) {
      fs.delete(in, true);
    }

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        fs.create(in)));

    Graph<CostVertex> wikipediaExampleGraph = TestGraphProvider
        .getWikipediaExampleGraph();
    for (CostVertex v : wikipediaExampleGraph.getVertexSet()) {
      Set<CostVertex> adjacentVertices = wikipediaExampleGraph
          .getAdjacentVertices(v);
      writer.write(v.getVertexId() + "\t" + toString(adjacentVertices));
      writer.write('\n');
    }
    writer.close();
  }

  private String toString(Set<CostVertex> adjacentVertices) {
    StringBuilder sb = new StringBuilder();
    for (CostVertex v : adjacentVertices) {
      sb.append(v.getVertexId());
      sb.append(':');
      sb.append(v.getCost());
      sb.append('\t');
    }
    return sb.toString();
  }

  private void verifyOutput(FileSystem fs, Path out) throws IOException {
    // TODO check the predecessors!
    int[] result = new int[10];
    int[] costs = new int[] { 0, 85, 217, 503, 173, 165, 403, 320, 415, 487 };
    FileStatus[] status = fs.listStatus(out);
    for (FileStatus fss : status) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          fs.open(fss.getPath())));

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
        String[] split = line.split("\t");
        result[Integer.parseInt(split[0])] = Integer.parseInt(split[2]);
      }
      reader.close();
    }

    for (int i = 0; i < result.length; i++) {
      assertEquals(costs[i], result[i]);
    }

  }
}
