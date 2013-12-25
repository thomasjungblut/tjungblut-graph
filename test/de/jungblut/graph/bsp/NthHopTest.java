package de.jungblut.graph.bsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
import org.junit.Test;

import com.google.common.collect.TreeMultimap;

import de.jungblut.graph.bsp.MindistSearch.TabToTextVertexReader;
import de.jungblut.graph.bsp.NthHop.EmptyVertexOutputWriter;
import de.jungblut.graph.bsp.NthHop.HopMessage;
import de.jungblut.graph.bsp.NthHop.NthHopVertex;

public final class NthHopTest {

  @Test
  public void testThreeHops() throws Exception {

    // Graph job configuration
    HamaConfiguration conf = new HamaConfiguration();
    conf.set("bsp.local.tasks.maximum", "1");
    conf.set(NthHop.MAX_HOPS_KEY, "3");
    GraphJob job = new GraphJob(conf, NthHop.class);
    FileSystem fs = FileSystem.get(conf);
    Path in = new Path("/tmp/nthhop/input.txt");
    createInput(fs, in);
    Path out = new Path("/tmp/nthhop/out/");
    if (fs.exists(out)) {
      fs.delete(out, true);
    }
    job.setInputPath(in);
    job.setOutputPath(out);
    job.setMaxIteration(3);
    job.setVertexClass(NthHopVertex.class);
    job.setVertexIDClass(Text.class);
    job.setVertexValueClass(HopMessage.class);
    job.setEdgeValueClass(NullWritable.class);

    job.setInputKeyClass(LongWritable.class);
    job.setInputValueClass(Text.class);
    job.setInputFormat(TextInputFormat.class);
    job.setVertexInputReaderClass(TabToTextVertexReader.class);
    job.setVertexOutputWriterClass(EmptyVertexOutputWriter.class);
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
      writer.write("1\t2\t3\n");
      writer.write("2\t4\n");
      writer.write("3\t4\t5\n");
      writer.write("4\t1\t5\t6\n");
      writer.write("5\t6\n");
      writer.write("6\n");
    }
  }

  private void verifyOutput(FileSystem fs, Path out) throws IOException {
    TreeMultimap<String, String> multiMap = TreeMultimap.create();
    FileStatus[] status = fs.listStatus(out);
    for (FileStatus fss : status) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          fs.open(fss.getPath())))) {

        String line;
        while ((line = reader.readLine()) != null) {
          String[] split = line.split("\t");
          multiMap.put(split[0], split[1]);
        }
      }
    }
    System.out.println(multiMap);
    // LORD forgive me this toString usage!
    Assert.assertEquals(4, multiMap.keySet().size());
    Assert.assertEquals("[1, 5, 6]", multiMap.get("1").toString());
    Assert.assertEquals("[2, 3, 6]", multiMap.get("2").toString());
    Assert.assertEquals("[2, 3, 6]", multiMap.get("3").toString());
    Assert.assertEquals("[4, 5]", multiMap.get("4").toString());

  }
}
