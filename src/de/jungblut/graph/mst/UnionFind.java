package de.jungblut.graph.mst;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Supporting data structure for the MST algorithm @{@link Kruskal}.
 *
 * @param <VERTEX_ID> the vertex id type
 */
public class UnionFind<VERTEX_ID> {

    class Node {
        Node parent;
        VERTEX_ID id;
        int numNodes;

        @Override
        public String toString() {
            return "Node{" +
                    "parent=" + (parent == null ? "null" : parent.id) +
                    ", id=" + id +
                    ", numNodes=" + numNodes +
                    '}';
        }
    }

    Node addDisjointedVertex(VERTEX_ID v) {
        checkNotNull(v);
        Node node = new Node();
        node.id = v;
        node.numNodes = 1;
        return node;
    }

    Node find(Node n) {
        Node root = n;
        while (root.parent != null) {
            root = root.parent;
        }

        return root;
    }

    void union(Node u, Node v) {
        Node uRoot = find(u);
        Node vRoot = find(v);

        // same set already
        if (uRoot == vRoot) {
            return;
        }

        // swap when the left tree is smaller for more efficient union
        if (uRoot.numNodes < vRoot.numNodes) {
            Node tmp = uRoot;
            uRoot = vRoot;
            vRoot = tmp;
        }

        // we union by making vRoot's parent uRoot
        vRoot.parent = uRoot;
        uRoot.numNodes += vRoot.numNodes;
    }


}
