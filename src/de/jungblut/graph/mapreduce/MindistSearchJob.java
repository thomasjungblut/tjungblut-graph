package de.jungblut.graph.mapreduce;

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

import java.io.IOException;

public class MindistSearchJob {

    private static final String FILES_IN = "files/min-search/import/";
    private static final String FILES_OUT = "files/min-search/depth_";

    public static void main(String[] args) throws IOException,
            InterruptedException, ClassNotFoundException {

        String inPath = null;
        if (args.length > 0) {
            inPath = args[0];
        }

        int depth = 1;
        Configuration conf = new Configuration();
        conf.set("recursion.depth", depth + "");
        Job job = Job.getInstance(conf);
        job.setJobName("Mindist Search");

        job.setMapperClass(TextGraphMapper.class);
        job.setReducerClass(MindistSearchReducer.class);
        job.setJarByClass(TextGraphMapper.class);

        Path in = new Path(inPath == null ? FILES_IN : inPath);
        Path out = new Path(FILES_OUT + 1);

        FileInputFormat.addInputPath(job, in);
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(out)) {
            fs.delete(out, true);
        }

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
            job = Job.getInstance(conf);
            job.setJobName("Mindist Search " + depth);

            job.setMapperClass(MindistSearchMapper.class);
            job.setReducerClass(MindistSearchReducer.class);
            job.setJarByClass(MindistSearchMapper.class);

            in = new Path(FILES_OUT + (depth - 1) + "/");
            out = new Path(FILES_OUT + depth);

            FileInputFormat.addInputPath(job, in);
            if (fs.exists(out)) {
                fs.delete(out, true);
            }

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
        out = new Path(FILES_OUT + "final");
        if (fs.exists(out)) {
            fs.delete(out, true);
        }
        fs.rename(new Path(FILES_OUT + (depth - 1)), out);

    }
}
