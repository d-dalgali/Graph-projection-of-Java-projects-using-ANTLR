Graph Projection of Java projects
Implement a tool that creates a graph that shows which methods are declared, and which methods are called by which methods. The nodes of the graph are methods and the directed edges go from the caller to the callee. 
For example, given the following code file, the tool should produce the directed graph on the right showing the call graph:

![image](https://user-images.githubusercontent.com/84984574/211995795-29873535-1046-40a1-b191-6ca6a6d320a7.png)

We will use a Java grammar written for Antlr4

