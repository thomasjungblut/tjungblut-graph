/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jungblut.graph.bsp;

import de.jungblut.graph.bsp.MindistSearch.MindistSearchVertex;
import de.jungblut.graph.bsp.MindistSearch.TabToTextVertexReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.BSPPeer;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.DefaultVertexOutputWriter;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.GraphJobMessage;
import org.apache.hama.graph.Vertex;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NthHop {

    public static final String MAX_HOPS_KEY = "graph.max.hops";

    public static class NthHopVertex extends
            Vertex<Text, NullWritable, HopMessage> {

        @Override
        public void compute(Iterable<HopMessage> messages) throws IOException {
            final int maxHops = getConf().getInt(MAX_HOPS_KEY, 3);
            if (getSuperstepCount() == 0L) {
                HopMessage msg = new HopMessage();
                msg.origin = getVertexID().toString();
                msg.hopCounter = 0;
                sendMessageToNeighbors(msg);
            } else {
                for (HopMessage message : messages) {
                    // fork a new message to not change state somewhere else in case of a
                    // memory storage.
                    HopMessage copyMessage = new HopMessage();
                    copyMessage.origin = message.origin;
                    copyMessage.hopCounter = message.hopCounter + 1;
                    if (copyMessage.hopCounter == maxHops) {
                        getPeer().write(new Text(copyMessage.origin), getVertexID());
                        voteToHalt();
                    } else {
                        sendMessageToNeighbors(copyMessage);
                    }
                }
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: <input> <output> <number of hops> [#tasks]");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException,
            InterruptedException, ClassNotFoundException {
        if (args.length < 3) {
            printUsage();
        }

        final int numHops = Integer.parseInt(args[2]);
        System.out.println("Number of hops: " + numHops);
        HamaConfiguration conf = new HamaConfiguration(new Configuration());
        GraphJob job = new GraphJob(conf, MindistSearchVertex.class);
        job.setJobName("Nth Hop Search");

        job.setVertexClass(NthHopVertex.class);
        job.setInputPath(new Path(args[0]));
        job.setOutputPath(new Path(args[1]));

        // set the defaults
        job.setMaxIteration(numHops);
        if (args.length == 4) {
            job.setNumBspTask(Integer.parseInt(args[3]));
        }

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
    }

    public static class EmptyVertexOutputWriter extends
            DefaultVertexOutputWriter<Text, NullWritable, HopMessage> {
        @Override
        public void write(Vertex<Text, NullWritable, HopMessage> vertex,
                          BSPPeer<Writable, Writable, Text, HopMessage, GraphJobMessage> peer)
                throws IOException {
            // do nothing, output is handled by the computation itself :)
        }
    }

    public static class HopMessage implements Writable {
        String origin;
        int hopCounter;

        @Override
        public void readFields(DataInput in) throws IOException {
            origin = in.readUTF();
            hopCounter = in.readInt();
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(origin);
            out.writeInt(hopCounter);
        }

        @Override
        public String toString() {
            return this.origin + " (" + this.hopCounter + ")";
        }

    }

}
