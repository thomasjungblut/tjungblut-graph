package de.jungblut.graph.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class MindistSearchReducer extends
    Reducer<LongWritable, VertexWritable, LongWritable, VertexWritable> {

  static final Log LOG = LogFactory.getLog(MindistSearchReducer.class);

  public static enum UpdateCounter {
    UPDATED
  }

  boolean depthOne;

  @Override
  protected void setup(Context context) throws IOException,
      InterruptedException {
    super.setup(context);
    if (Integer.parseInt(context.getConfiguration().get("recursion.depth")) == 1)
      depthOne = true;
  }

  @Override
  protected void reduce(LongWritable key, Iterable<VertexWritable> values,
      Context context) throws IOException, InterruptedException {

    VertexWritable realVertex = null;
    LongWritable currentMinimalKey = null;

    if (depthOne) {
      for (VertexWritable vertex : values) {
        if (!vertex.isMessage()) {
          realVertex = vertex.clone();
        }
      }
      realVertex.setActivated(true);
      realVertex.setVertexId(realVertex.getEdges().first());
      if (key.get() < realVertex.getVertexId().get())
        realVertex.setVertexId(key);
      context.getCounter(UpdateCounter.UPDATED).increment(1);
    } else {
      for (VertexWritable vertex : values) {
        if (!vertex.isMessage()) {
          if (realVertex == null) {
            realVertex = vertex.clone();
          }
        } else {
          if (currentMinimalKey == null) {
            currentMinimalKey = new LongWritable(vertex.getVertexId().get());
          } else {
            if (currentMinimalKey.get() > vertex.getVertexId().get()) {
              currentMinimalKey = new LongWritable(vertex.getVertexId().get());
            }
          }
        }
      }

      if (currentMinimalKey != null
          && currentMinimalKey.get() < realVertex.getVertexId().get()) {
        realVertex.setVertexId(currentMinimalKey);
        realVertex.setActivated(true);
        context.getCounter(UpdateCounter.UPDATED).increment(1);
      } else {
        realVertex.setActivated(false);
      }
    }

    context.write(key, realVertex);
    LOG.info(key + " | " + realVertex);
  }

}
