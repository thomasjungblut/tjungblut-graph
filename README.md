![Java CI with Maven](https://github.com/thomasjungblut/tjungblut-graph/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)

## tjungblut-graph

This is my take on a Graph library. The starting point was more than ten years ago during [GSoC2012 on Google Code](https://code.google.com/p/hama-shortest-paths/). 

I wanted to get out all the MapReduce and BSP related algorithms that I featured on my blog, like mindist search, pagerank or SSSP. 
This repository also features many interesting other graph algorithms, in either MapReduce, BSP or a sequential version.

Over the years, those algorithms have accumulated:
* Traversal: BFS, DFS (both as a Java Iterator)
* Shortest Paths: Dijkstra, A*, Bellman Ford
* MinCut: Stoer-Wagner 
* Minimum Spanning Tree: Prim + Kruskal including a UnionFind datastructure
* Topological Sort
* Misc: Degree Counter, Triangle Counter
* In Bulk Synchronous Parallel: n-th Hop, Mindist Search, SSSP
* In MapReduce: Mindist Search


License
-------

Since I am Apache committer, I consider everything inside of this repository 
licensed by Apache 2.0 license, although I haven't put the usual header into the source files.

If something is not licensed via Apache 2.0, there is a reference or an additional licence header included in the specific source file.


Build
-----

You can simply build with:
 
> mvn clean package install

The created jar contains debuggable code + sources.
