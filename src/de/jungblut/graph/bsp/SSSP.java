package de.jungblut.graph.bsp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

/**
 * Single Source Shortest Paths with predecessor logic that a shortest path can
 * always be reconstructed from an end vertex.
 * 
 * @author thomas.jungblut
 * 
 */
public final class SSSP {

  public static final String START_VERTEX = "shortest.paths.start.vertex.name";

  // a vertex ID is text and we are using a id/distance tuple for shortest paths
  public static class ShortestPathVertex extends
      Vertex<Text, IntWritable, TextIntPairWritable> {

    @Override
    public void setup(Configuration conf) {
      this.setValue(new TextIntPairWritable(this.getVertexID(),
          new IntWritable(Integer.MAX_VALUE)));
    }

    public boolean isStartVertex() {
      Text startVertex = new Text(getConf().get(START_VERTEX));
      return (this.getVertexID().equals(startVertex)) ? true : false;
    }

    @Override
    public void compute(Iterator<TextIntPairWritable> messages)
        throws IOException {
      int minDist = isStartVertex() ? 0 : Integer.MAX_VALUE;
      Text minPredecessor = getVertexID();
      while (messages.hasNext()) {
        TextIntPairWritable msg = messages.next();
        if (msg.getDistance().get() < minDist) {
          minDist = msg.getDistance().get();
          minPredecessor = msg.getId();
        }
      }

      if (minDist < this.getValue().getDistance().get()) {
        this.setValue(new TextIntPairWritable(minPredecessor, new IntWritable(
            minDist)));
        for (Edge<Text, IntWritable> e : this.getEdges()) {
          sendMessage(e, new TextIntPairWritable(this.getVertexID(),
              new IntWritable(minDist + e.getValue().get())));
        }
      } else {
        voteToHalt();
      }
    }
  }

  public static class SSSPTextReader
      extends
      VertexInputReader<LongWritable, Text, Text, IntWritable, TextIntPairWritable> {

    /**
     * The text file essentially should look like: <br/>
     * VERTEX_ID\t(n-tab separated VERTEX_ID:VERTEX_VALUE pairs)<br/>
     * E.G:<br/>
     * 1\t2:25\t3:32\t4:21<br/>
     * 2\t3:222\t1:922<br/>
     * etc.
     */
    @Override
    public boolean parseVertex(LongWritable key, Text value,
        Vertex<Text, IntWritable, TextIntPairWritable> vertex) {
      String[] split = value.toString().split("\t");
      for (int i = 0; i < split.length; i++) {
        if (i == 0) {
          vertex.setVertexID(new Text(split[i]));
        } else {
          String[] split2 = split[i].split(":");
          vertex.addEdge(new Edge<Text, IntWritable>(new Text(split2[0]),
              new IntWritable(Integer.parseInt(split2[1]))));
        }
      }
      return true;
    }

  }

  static class TextIntPairWritable implements Writable {

    public Text id;
    public IntWritable distance;

    public TextIntPairWritable() {
    }

    public TextIntPairWritable(Text id, IntWritable distance) {
      super();
      this.id = id;
      this.distance = distance;
    }

    public Text getId() {
      return id;
    }

    public IntWritable getDistance() {
      return distance;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
      id = new Text();
      distance = new IntWritable();
      id.readFields(in);
      distance.readFields(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
      id.write(out);
      distance.write(out);
    }

    @Override
    public String toString() {
      return id + "\t" + distance;
    }

  }

  private static void printUsage() {
    System.out.println("Usage: <startnode> <input> <output> [tasks]");
    System.exit(-1);
  }

  public static void main(String[] args) throws IOException,
      InterruptedException, ClassNotFoundException {
    if (args.length < 3)
      printUsage();

    // Graph job configuration
    HamaConfiguration conf = new HamaConfiguration();
    GraphJob ssspJob = new GraphJob(conf, SSSP.class);
    // Set the job name
    ssspJob.setJobName("Single Source Shortest Path");

    conf.set(START_VERTEX, args[0]);
    ssspJob.setInputPath(new Path(args[1]));
    ssspJob.setOutputPath(new Path(args[2]));

    if (args.length == 4) {
      ssspJob.setNumBspTask(Integer.parseInt(args[3]));
    }

    ssspJob.setVertexClass(ShortestPathVertex.class);
    ssspJob.setInputFormat(TextInputFormat.class);
    ssspJob.setInputKeyClass(LongWritable.class);
    ssspJob.setInputValueClass(Text.class);

    ssspJob.setPartitioner(HashPartitioner.class);
    ssspJob.setOutputFormat(TextOutputFormat.class);
    ssspJob.setVertexInputReaderClass(SSSPTextReader.class);
    ssspJob.setOutputKeyClass(Text.class);
    ssspJob.setOutputValueClass(TextIntPairWritable.class);
    // Iterate until all the nodes have been reached.
    ssspJob.setMaxIteration(Integer.MAX_VALUE);

    ssspJob.setVertexIDClass(Text.class);
    ssspJob.setVertexValueClass(TextIntPairWritable.class);
    ssspJob.setEdgeValueClass(IntWritable.class);

    long startTime = System.currentTimeMillis();
    if (ssspJob.waitForCompletion(true)) {
      System.out.println("Job Finished in "
          + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
    }
  }
}
