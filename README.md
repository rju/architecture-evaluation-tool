# Architecture Evaluation Tool

The present tool is designed to analyze Java source based on a hypergraph metric from Edward B. Allen (2007) [1].
Based on the abstract representation in a hypergraph, the tool can and will be extended to support other languages,
like Xtend and Groovy. For the analyses the tool requires two distinct inputs:
a) Classes which represent data types must be mentioned in a data-type-pattern.cfg file. This includes data type
   classes of Java, like java.lang.String. The file accepts wildcards in its pattern.
b) Classes which belong to the observed system and must be represented in the hypergraph must be listed in a 
   similar way in a file called observed-system.cfg

When both files are in place, the user can select a Java project in Eclipse. Depending on the size of the project
and the used framework and library functionalities, the analysis could take hours. Presently, the tool parallelizes
certain operations assuming that the machine has 8 CPUs. However, this should be configurable in future.

[1] Edward B. Allen, 2007; Measuring size, complexity, and coupling of hypergraph abstractions of
    software: An information-theory approach
