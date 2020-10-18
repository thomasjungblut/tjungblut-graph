package de.jungblut.graph.partition;

import de.jungblut.graph.AdjacencyList;
import de.jungblut.graph.Graph;
import de.jungblut.graph.Tuple;
import de.jungblut.graph.model.Edge;
import de.jungblut.graph.model.Vertex;
import de.jungblut.graph.model.VertexImpl;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the Stoer-Wagner MinCut algorithm using non-negative integer weights.
 * This follows the paper from https://fktpm.ru/file/204-stoer-wagner-a-simple-min-cut-algorithm.pdf.
 */
public class StoerWagnerMinCut<VERTEX_ID, VERTEX_VALUE> {

    public class MinCut {
        private final Graph<VERTEX_ID, VERTEX_VALUE, Integer> first;
        private final Graph<VERTEX_ID, VERTEX_VALUE, Integer> second;
        private final List<Tuple<Vertex<VERTEX_ID, VERTEX_VALUE>, Edge<VERTEX_ID, Integer>>> edgesOnTheCut;
        private final int minCutWeight;

        public MinCut(Graph<VERTEX_ID, VERTEX_VALUE, Integer> first,
                      Graph<VERTEX_ID, VERTEX_VALUE, Integer> second,
                      List<Tuple<Vertex<VERTEX_ID, VERTEX_VALUE>, Edge<VERTEX_ID, Integer>>> edgesOnTheCut,
                      int minCutWeight) {
            this.first = first;
            this.second = second;
            this.edgesOnTheCut = edgesOnTheCut;
            this.minCutWeight = minCutWeight;
        }

        public int getMinCutWeight() {
            return minCutWeight;
        }

        public Graph<VERTEX_ID, VERTEX_VALUE, Integer> getFirst() {
            return first;
        }

        public Graph<VERTEX_ID, VERTEX_VALUE, Integer> getSecond() {
            return second;
        }

        public List<Tuple<Vertex<VERTEX_ID, VERTEX_VALUE>, Edge<VERTEX_ID, Integer>>> getEdgesOnTheCut() {
            return edgesOnTheCut;
        }
    }

    static class CutOfThePhase<VERTEX_ID> {
        final VERTEX_ID s;
        final VERTEX_ID t;
        final int weight;

        public CutOfThePhase(VERTEX_ID s, VERTEX_ID t, int weight) {
            this.s = s;
            this.t = t;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "CutOfThePhase{" +
                    "s=" + s +
                    ", t=" + t +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * Computes the minimum cut (MinCut) of the given non-negatively weighted graph.
     * Running the algorithm results in two disjoint subgraphs, so that the sum of
     * weights between these two subgraphs is minimal.
     *
     * @param g weighted graph you want to cut in half
     * @return a @{@link MinCut} that contains both the first and second disjoint graph,
     * the removed edges that lead to that partition and their summed-up weight.
     */
    public MinCut computeMinCut(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g) {
        checkArgument(g.getNumVertices() >= 2, "graph must have at least two vertices");
        // validate that the graph has only positive weights
        for (VERTEX_ID v : g.getVertexIDSet()) {
            for (Edge<VERTEX_ID, Integer> e : g.getEdges(v)) {
                checkArgument(e.getValue() >= 0, "graph must have positive edge weights");
            }
        }

        Graph<VERTEX_ID, VERTEX_VALUE, Integer> originalGraph = g;
        Set<VERTEX_ID> currentPartition = new HashSet<>();
        Set<VERTEX_ID> currentBestPartition = null;
        CutOfThePhase<VERTEX_ID> currentBestCut = null;
        while (g.getNumVertices() > 1) {
            CutOfThePhase<VERTEX_ID> cutOfThePhase = maximumAdjacencySearch(g);
            if (currentBestCut == null || cutOfThePhase.weight < currentBestCut.weight) {
                currentBestCut = cutOfThePhase;
                currentBestPartition = new HashSet<>(currentPartition);
                currentBestPartition.add(cutOfThePhase.t);
            }
            currentPartition.add(cutOfThePhase.t);
            // merge s and t and their edges together
            g = mergeVerticesFromCut(g, cutOfThePhase);
        }

        return constructMinCutResult(originalGraph, currentBestPartition, currentBestCut);
    }

    // we do a two-pass algorithm to reconstruct the sub-graphs:
    // - first we distribute the vertices into their respective graph
    // - second we align edges: if they cross graphs they will end in the list, otherwise in the respective graph
    private MinCut constructMinCutResult(Graph<VERTEX_ID, VERTEX_VALUE, Integer> originalGraph,
                                         Set<VERTEX_ID> partition,
                                         CutOfThePhase<VERTEX_ID> currentBestCut) {
        Graph<VERTEX_ID, VERTEX_VALUE, Integer> first = new AdjacencyList<>();
        Graph<VERTEX_ID, VERTEX_VALUE, Integer> second = new AdjacencyList<>();
        List<Tuple<Vertex<VERTEX_ID, VERTEX_VALUE>, Edge<VERTEX_ID, Integer>>> cuttingEdges = new ArrayList<>();
        int cutWeight = 0;

        for (VERTEX_ID v : originalGraph.getVertexIDSet()) {
            if (partition.contains(v)) {
                first.addVertex(new VertexImpl<>(v, originalGraph.getVertex(v).getVertexValue()));
            } else {
                second.addVertex(new VertexImpl<>(v, originalGraph.getVertex(v).getVertexValue()));
            }
        }

        for (VERTEX_ID v : originalGraph.getVertexIDSet()) {
            Set<Edge<VERTEX_ID, Integer>> edges = originalGraph.getEdges(v);
            for (Edge<VERTEX_ID, Integer> e : edges) {
                if (first.getVertexIDSet().contains(v) && first.getVertexIDSet().contains(e.getDestinationVertexID())) {
                    first.addEdge(v, new Edge<>(e.getDestinationVertexID(), e.getValue()));
                } else if (second.getVertexIDSet().contains(v) && second.getVertexIDSet().contains(e.getDestinationVertexID())) {
                    second.addEdge(v, new Edge<>(e.getDestinationVertexID(), e.getValue()));
                } else {
                    cuttingEdges.add(new Tuple<>(originalGraph.getVertex(v), new Edge<>(e.getDestinationVertexID(), e.getValue())));
                    cutWeight += e.getValue();
                }
            }
        }

        return new MinCut(first, second, cuttingEdges, cutWeight);
    }

    // bascially we're copying the whole graph into a new one, we leave the vertex "t" out of it (including the edge between "s" and "t")
    // and merge all other edges that "s" and "t" pointed together towards by summing their weight.
    // there might be left-over edges pointing from "t" but not through "s", we have to attach them to "s" as well.
    Graph<VERTEX_ID, VERTEX_VALUE, Integer> mergeVerticesFromCut(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g, CutOfThePhase<VERTEX_ID> cutOfThePhase) {
        Graph<VERTEX_ID, VERTEX_VALUE, Integer> toReturn = new AdjacencyList<>();

        for (VERTEX_ID v : g.getVertexIDSet()) {
            boolean isS = cutOfThePhase.s.equals(v);
            boolean isT = cutOfThePhase.t.equals(v);
            // plain copy
            if (!isS && !isT) {
                toReturn.addVertex(g.getVertex(v));
                for (Edge<VERTEX_ID, Integer> e : g.getEdges(v)) {
                    if (!e.getDestinationVertexID().equals(cutOfThePhase.s) && !e.getDestinationVertexID().equals(cutOfThePhase.t)) {
                        toReturn.addEdge(v, new Edge<>(e.getDestinationVertexID(), e.getValue()));
                    }
                }
            }

            // on hitting "s" we are checking for the summation case (similar edge coming from "t"), otherwise just copy it over
            if (isS) {
                toReturn.addVertex(g.getVertex(v));
                for (Edge<VERTEX_ID, Integer> e : g.getEdges(v)) {
                    if (e.getDestinationVertexID().equals(cutOfThePhase.t)) {
                        continue;
                    }
                    Edge<VERTEX_ID, Integer> mergableEdge = g.getEdge(cutOfThePhase.t, e.getDestinationVertexID());
                    if (mergableEdge != null) {
                        toReturn.addEdge(v, new Edge<>(e.getDestinationVertexID(), e.getValue() + mergableEdge.getValue()));
                        toReturn.addEdge(e.getDestinationVertexID(), new Edge<>(v, e.getValue() + mergableEdge.getValue()));
                    } else {
                        toReturn.addEdge(v, new Edge<>(e.getDestinationVertexID(), e.getValue()));
                        toReturn.addEdge(e.getDestinationVertexID(), new Edge<>(v, e.getValue()));
                    }
                }
            }
        }

        // for all edges from "t" that we haven't transferred to "s" yet, but do not go towards "s"
        for (Edge<VERTEX_ID, Integer> e : g.getEdges(cutOfThePhase.t)) {
            if (e.getDestinationVertexID().equals(cutOfThePhase.s)) {
                continue;
            }
            Edge<VERTEX_ID, Integer> transferEdge = g.getEdge(cutOfThePhase.s, e.getDestinationVertexID());
            if (transferEdge == null) {
                toReturn.addEdge(cutOfThePhase.s, new Edge<>(e.getDestinationVertexID(), e.getValue()));
                toReturn.addEdge(e.getDestinationVertexID(), new Edge<>(cutOfThePhase.s, e.getValue()));
            }
        }


        return toReturn;
    }

    private CutOfThePhase<VERTEX_ID> maximumAdjacencySearch(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g) {
        return maximumAdjacencySearch(g, null);
    }

    /**
     * This iterates the given graph starting from "start" in a maximum adjacency fashion.
     * That means, it will always connect to the next vertex whose inlinks are having the largest edge weight sum.
     * This ordering implicitly gives us a "cut of the phase", as the last two added vertices are the ones with least inlink weights.
     */
    CutOfThePhase<VERTEX_ID> maximumAdjacencySearch(Graph<VERTEX_ID, VERTEX_VALUE, Integer> g, VERTEX_ID start) {
        if (start == null) {
            start = g.getVertexIDSet().iterator().next();
        }
        List<VERTEX_ID> maxAdjacencyOrderedList = new ArrayList<>(Collections.singletonList(start));
        List<Integer> cutWeight = new ArrayList<>();
        Set<VERTEX_ID> candidates = new HashSet<>(g.getVertexIDSet());
        candidates.remove(start);

        while (!candidates.isEmpty()) {
            VERTEX_ID maxNextVertex = null;
            int maxWeight = Integer.MIN_VALUE;
            for (VERTEX_ID next : candidates) {
                // compute the inlink weight sum from all vertices in "maxAdjacencyOrderedList" towards vertex "next"
                int weightSum = 0;
                for (VERTEX_ID s : maxAdjacencyOrderedList) {
                    Edge<VERTEX_ID, Integer> edge = g.getEdge(next, s);
                    if (edge != null) {
                        weightSum += edge.getValue();
                    }
                }

                if (weightSum > maxWeight) {
                    maxNextVertex = next;
                    maxWeight = weightSum;
                }
            }

            candidates.remove(maxNextVertex);
            maxAdjacencyOrderedList.add(maxNextVertex);
            cutWeight.add(maxWeight);
        }

        // we take the last two vertices in that list and their weight as a cut of the phase
        int n = maxAdjacencyOrderedList.size();
        return new CutOfThePhase<>(
                maxAdjacencyOrderedList.get(n - 2), // that's "s" in the literature and will remain as a merged vertex with "t"
                maxAdjacencyOrderedList.get(n - 1), // that's "t" and will be removed afterwards
                cutWeight.get(cutWeight.size() - 1)); // that's "w" to compute the minimum cut on later
    }


}
