package de.jungblut.graph.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

public class VertexWritable implements Writable, Cloneable {

  LongWritable minimalVertexId;
  TreeSet<LongWritable> pointsTo;
  boolean activated;

  public VertexWritable() {
    super();
  }

  public VertexWritable(LongWritable minimalVertexId) {
    super();
    this.minimalVertexId = minimalVertexId;
  }

  // true if updated
  public boolean checkAndSetMinimalVertex(LongWritable id) {
    if (minimalVertexId == null) {
      minimalVertexId = id;
      return true;
    } else {
      if (id.get() < minimalVertexId.get()) {
        minimalVertexId = id;
        return true;
      }
    }
    return false;
  }

  public boolean isMessage() {
    if (pointsTo == null)
      return true;
    else
      return false;
  }

  public VertexWritable makeMessage() {
    return new VertexWritable(minimalVertexId);
  }

  public void addVertex(LongWritable id) {
    if (pointsTo == null)
      pointsTo = new TreeSet<LongWritable>();
    pointsTo.add(id);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    minimalVertexId.write(out);
    if (pointsTo == null) {
      out.writeInt(-1);
    } else {
      out.writeInt(pointsTo.size());
      for (LongWritable l : pointsTo) {
        l.write(out);
      }
    }
    out.writeBoolean(activated);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    minimalVertexId = new LongWritable();
    minimalVertexId.readFields(in);
    int length = in.readInt();
    if (length > -1) {
      pointsTo = new TreeSet<LongWritable>();
      for (int i = 0; i < length; i++) {
        LongWritable temp = new LongWritable();
        temp.readFields(in);
        pointsTo.add(temp);
      }
    } else {
      pointsTo = null;
    }
    activated = in.readBoolean();
  }

  @Override
  public String toString() {
    return "VertexWritable [minimalVertexId=" + minimalVertexId + ", pointsTo="
        + pointsTo + "]";
  }

  @Override
  protected VertexWritable clone() {
    VertexWritable toReturn = new VertexWritable(new LongWritable(
        minimalVertexId.get()));
    if (pointsTo != null) {
      toReturn.pointsTo = new TreeSet<LongWritable>();
      for (LongWritable l : pointsTo) {
        toReturn.pointsTo.add(new LongWritable(l.get()));
      }
    }
    return toReturn;
  }

  public boolean isActivated() {
    return activated;
  }
}
