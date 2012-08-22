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
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;
import org.junit.Test;

import de.jungblut.graph.Graph;
import de.jungblut.graph.TestGraphProvider;
import de.jungblut.graph.bsp.MindistSearch.MindistSearchCountReader;
import de.jungblut.graph.bsp.MindistSearch.MindistSearchVertex;
import de.jungblut.graph.vertex.CostVertex;

public final class MindistSearchTest extends TestCase {

  @Test
  public void testSSSP() throws Exception {

    // Graph job configuration
    HamaConfiguration conf = new HamaConfiguration();
    conf.set("bsp.local.tasks.maximum", "2");
    GraphJob job = new GraphJob(conf, MindistSearch.class);
    FileSystem fs = FileSystem.get(conf);
    Path in = new Path("/tmp/mdst/input.txt");
    createInput(fs, in);
    Path out = new Path("/tmp/mdst/out/");
    if (fs.exists(out)) {
      fs.delete(out, true);
    }
    job.setInputPath(in);
    job.setOutputPath(out);

    job.setVertexClass(MindistSearchVertex.class);
    job.setVertexIDClass(Text.class);
    job.setVertexValueClass(Text.class);
    job.setEdgeValueClass(NullWritable.class);

    job.setInputKeyClass(LongWritable.class);
    job.setInputValueClass(Text.class);
    job.setInputFormat(TextInputFormat.class);
    job.setVertexInputReaderClass(MindistSearchCountReader.class);
    job.setPartitioner(HashPartitioner.class);
    job.setOutputFormat(TextOutputFormat.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    job.setVertexIDClass(Text.class);
    job.setVertexValueClass(IntWritable.class);
    job.setEdgeValueClass(IntWritable.class);

    long startTime = System.currentTimeMillis();
    if (job.waitForCompletion(true)) {
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
      sb.append('\t');
    }
    return sb.toString();
  }

  private void verifyOutput(FileSystem fs, Path out) throws IOException {
    int[] result = new int[10];
    FileStatus[] status = fs.listStatus(out);
    for (FileStatus fss : status) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          fs.open(fss.getPath())));

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
        String[] split = line.split("\t");
        result[Integer.parseInt(split[0])] = Integer.parseInt(split[1]);
      }
      reader.close();
    }

    for (int i = 0; i < result.length; i++) {
      assertEquals(0, result[i]);
    }

  }
}
