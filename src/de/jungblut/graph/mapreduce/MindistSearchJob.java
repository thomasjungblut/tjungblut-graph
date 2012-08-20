package de.jungblut.graph.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class MindistSearchJob {

  public static void main(String[] args) throws IOException,
      InterruptedException, ClassNotFoundException {
    int depth = 1;
    Configuration conf = new Configuration();
    conf.set("recursion.depth", depth + "");
    Job job = new Job(conf);
    job.setJobName("Graph explorer");

    job.setMapperClass(TextGraphMapper.class);
    job.setReducerClass(MindistSearchReducer.class);
    job.setJarByClass(TextGraphMapper.class);

    Path in = new Path("files/graph-exploration/import/");
    Path out = new Path("files/graph-exploration/depth_1");

    FileInputFormat.addInputPath(job, in);
    FileSystem fs = FileSystem.get(conf);
    if (fs.exists(out))
      fs.delete(out, true);

    FileOutputFormat.setOutputPath(job, out);
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(SequenceFileOutputFormat.class);
    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(VertexWritable.class);

    job.waitForCompletion(true);

    long counter = job.getCounters()
        .findCounter(MindistSearchReducer.UpdateCounter.UPDATED).getValue();
    depth++;
    while (counter > 0) {
      conf = new Configuration();
      conf.set("recursion.depth", depth + "");
      job = new Job(conf);
      job.setJobName("Graph explorer " + depth);

      job.setMapperClass(MindistSearchMapper.class);
      job.setReducerClass(MindistSearchReducer.class);
      job.setJarByClass(MindistSearchMapper.class);

      in = new Path("files/graph-exploration/depth_" + (depth - 1) + "/");
      out = new Path("files/graph-exploration/depth_" + depth);

      FileInputFormat.addInputPath(job, in);
      if (fs.exists(out))
        fs.delete(out, true);

      FileOutputFormat.setOutputPath(job, out);
      job.setInputFormatClass(SequenceFileInputFormat.class);
      job.setOutputFormatClass(SequenceFileOutputFormat.class);
      job.setOutputKeyClass(LongWritable.class);
      job.setOutputValueClass(VertexWritable.class);

      job.waitForCompletion(true);
      depth++;
      counter = job.getCounters()
          .findCounter(MindistSearchReducer.UpdateCounter.UPDATED).getValue();
    }

  }

}
