package de.jungblut.graph.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TextGraphMapper extends
        Mapper<LongWritable, Text, LongWritable, VertexWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        VertexWritable vertex = new VertexWritable();
        LongWritable realKey = null;
        String[] values = value.toString().split("\t");
        int currentCount = 0;
        for (String s : values) {
            if (currentCount == 0) {
                realKey = new LongWritable(Long.parseLong(s));
                vertex.checkAndSetMinimalVertex(realKey);
                vertex.addVertex(realKey);
            } else {
                LongWritable temp = new LongWritable(Long.parseLong(s));
                vertex.checkAndSetMinimalVertex(temp);
                vertex.addVertex(temp);
            }
            currentCount++;
        }

        context.write(realKey, vertex);

        for (LongWritable edge : vertex.getEdges()) {
            context.write(edge, vertex.makeMessage());
        }
    }

}
