package de.jungblut.graph.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class MindistSearchMapper extends
    Mapper<LongWritable, VertexWritable, LongWritable, VertexWritable> {

  @Override
  protected void map(LongWritable key, VertexWritable value, Context context)
      throws IOException, InterruptedException {

    context.write(key, value);
    if (value.isActivated()) {
      VertexWritable writable = new VertexWritable();
      for (LongWritable neighborVertex : value.getEdges()) {
        if (neighborVertex.get() != value.getVertexId().get()) {
          writable.setVertexId(value.getVertexId());
          writable.setEdges(null);
          context.write(neighborVertex, writable);
        }
      }
    }
  }

}
