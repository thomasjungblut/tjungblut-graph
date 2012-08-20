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
      realVertex.activated = true;
      realVertex.minimalVertexId = realVertex.pointsTo.first();
      if (key.get() < realVertex.minimalVertexId.get())
        realVertex.minimalVertexId = key;
      context.getCounter(UpdateCounter.UPDATED).increment(1);
    } else {
      for (VertexWritable vertex : values) {
        if (!vertex.isMessage()) {
          if (realVertex == null) {
            realVertex = vertex.clone();
          }
        } else {
          if (currentMinimalKey == null) {
            currentMinimalKey = new LongWritable(vertex.minimalVertexId.get());
          } else {
            if (currentMinimalKey.get() > vertex.minimalVertexId.get()) {
              currentMinimalKey = new LongWritable(vertex.minimalVertexId.get());
            }
          }
        }
      }

      if (currentMinimalKey != null
          && currentMinimalKey.get() < realVertex.minimalVertexId.get()) {
        realVertex.minimalVertexId = currentMinimalKey;
        realVertex.activated = true;
        context.getCounter(UpdateCounter.UPDATED).increment(1);
      } else {
        realVertex.activated = false;
      }
    }

    context.write(key, realVertex);
    LOG.info(key + " | " + realVertex);
  }

}
