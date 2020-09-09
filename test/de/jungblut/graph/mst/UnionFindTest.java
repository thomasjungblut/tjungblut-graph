package de.jungblut.graph.mst;

import org.junit.Assert;
import org.junit.Test;

public class UnionFindTest {

    @Test
    public void testUnionFindHappyPath() {
        UnionFind<String> uf = new UnionFind<>();
        UnionFind<String>.Node a = uf.addDisjointedVertex("a");
        UnionFind<String>.Node b = uf.addDisjointedVertex("b");

        // find should return itself
        Assert.assertSame(a, uf.find(a));
        Assert.assertSame(b, uf.find(b));

        // unioning both should result in one tree, so finding both should point to A
        uf.union(a, b);
        Assert.assertSame(a, uf.find(a));
        Assert.assertSame(a, uf.find(b));
        Assert.assertEquals(2, a.numNodes);
    }

    @Test
    public void testUnionWithinSameTree() {
        UnionFind<String> uf = new UnionFind<>();
        UnionFind<String>.Node a = uf.addDisjointedVertex("a");
        UnionFind<String>.Node b = uf.addDisjointedVertex("b");
        uf.union(a, b);
        Assert.assertSame(a, uf.find(a));
        Assert.assertSame(a, uf.find(b));
        // that should be idempontent
        uf.union(a, b);
        Assert.assertSame(a, uf.find(a));
        Assert.assertSame(a, uf.find(b));

    }

}
