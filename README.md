This is my own Graph package. 
The most of the stuff in here was from my [GSoC2012 repository here](https://code.google.com/p/hama-shortest-paths/). 
I wanted to get out all the batch related things that I featured on my blog, like mindist search and pagerank. 
But I have seen that my sequential part of the library is pretty damn bad, so I decided to make it cool and more generic.

This repository will also feature many interesting other graph algorithms, in either MapReduce, BSP or a sequential version.


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
