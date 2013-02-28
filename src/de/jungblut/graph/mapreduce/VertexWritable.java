package de.jungblut.graph.mapreduce;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

public final class VertexWritable implements Writable, Cloneable {

  private LongWritable vertexId;
  private TreeSet<LongWritable> edges;
  private boolean activated;

  public VertexWritable() {
    super();
  }

  public VertexWritable(LongWritable minimalVertexId) {
    super();
    this.vertexId = minimalVertexId;
  }

  // true if updated
  public boolean checkAndSetMinimalVertex(LongWritable id) {
    if (vertexId == null) {
      vertexId = id;
      return true;
    } else {
      if (id.get() < vertexId.get()) {
        vertexId = id;
        return true;
      }
    }
    return false;
  }

  public boolean isMessage() {
    if (edges == null)
      return true;
    else
      return false;
  }

  public VertexWritable makeMessage() {
    return new VertexWritable(vertexId);
  }

  public void addVertex(LongWritable id) {
    if (edges == null)
      edges = new TreeSet<>();
    edges.add(id);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    vertexId.write(out);
    if (edges == null) {
      out.writeInt(-1);
    } else {
      out.writeInt(edges.size());
      for (LongWritable l : edges) {
        l.write(out);
      }
    }
    out.writeBoolean(activated);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    vertexId = new LongWritable();
    vertexId.readFields(in);
    int length = in.readInt();
    if (length > -1) {
      edges = new TreeSet<>();
      for (int i = 0; i < length; i++) {
        LongWritable temp = new LongWritable();
        temp.readFields(in);
        edges.add(temp);
      }
    } else {
      edges = null;
    }
    activated = in.readBoolean();
  }

  @Override
  public String toString() {
    return "VertexWritable [minimalVertexId=" + vertexId + ", pointsTo="
        + edges + "]";
  }

  @Override
  protected VertexWritable clone() {
    VertexWritable toReturn = new VertexWritable(new LongWritable(
        vertexId.get()));
    if (edges != null) {
      toReturn.edges = new TreeSet<>();
      for (LongWritable l : edges) {
        toReturn.edges.add(new LongWritable(l.get()));
      }
    }
    return toReturn;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setVertexId(LongWritable vertexId) {
    this.vertexId = vertexId;
  }

  public void setEdges(TreeSet<LongWritable> edges) {
    this.edges = edges;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public LongWritable getVertexId() {
    return vertexId;
  }

  public TreeSet<LongWritable> getEdges() {
    return edges;
  }

}
