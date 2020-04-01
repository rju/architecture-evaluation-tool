# Architecture Evaluation Tool

The architecture evaluation tool supports the analysis of Java projects and GECO megamodels based on a hypergraph metric from Edward B. Allen (2007) [1]. The underlying framework allows to extend the analysis for other types of models and programming languages. this is achieved by
a) using frontend transfromations to create a hypergraph or modular hypergraph
b) a set of analysis metrics and transfromations for these hypergraphs and modular hypergraphs

See "adding support for a metamodel" in the wiki.

## Eclipse-Repository

A pre-compiled of the **architecture evaluation tool** can be found at
* snapshot version https://maui.se.informatik.uni-kiel.de/repo/se/snapshot/
* release versions https://maui.se.informatik.uni-kiel.de/repo/se/releases/ (when they come available)

## Dependencies

Tested with:
* maven 3.5.4, 3.6.0
* Java 8 (oracle version) does not compile with Java 9 or higher
* Eclipse Oxygen

Operating systems:
* Ubuntu 16.04, 17.10, 18.04, 18.10
* MacOS
* Windows 7 + maven 3.6.0 + Java 8

**Note:** It has been reported that it does not compile on Windows using the Windows Linux Subsystem.

## Compiling the tool yourself

You have two option to compile the **architecture evaluation tool**. Our primary build system is maven, which we recommend. However, you can also compile it using your Eclipse IDE.

### Maven Build

The **architecture evaluation tool** uses maven as build system. To compile the plugins
* git clone https://github.com/rju/architecture-evaluation-tool.git
* mvn compile
* mvn package (currently you need to modify the pom.xml in de.cau.cs.se.software.evaluation.repository to be able to deploy the repository to your own location)
* Add the repository location to your eclipse update sites and install the tool

## Eclipse Build

* Import project as maven projects
* Set API-baseline
* Required plugins for Eclipse which you should install in advance
  ** [PCM v4.0](https://sdqweb.ipd.kit.edu/wiki/PCM_Installation) is necessary for the PCM evaluation part
  ** [Geco](https://maui.se.informatik.uni-kiel.de/repo/geco/release/1.0.0 or https://maui.se.informatik.uni-kiel.de/repo/geco/snapshot) provides basic infrastructure for all transformations
  ** [Klighd](http://rtsys.informatik.uni-kiel.de/~kieler/updatesite/nightly-openkieler/) (Without Ptolemy) used for graph visualization
* Trigger clean and build 

## Using the Metrics with your Models and Code

### Views

- **Analysis Result View** list results regarding the hypergraph creation and the different metric results
- **Modular Hypergraph Diagram** view with modules and nodes
- **Hypergraph Diagram** view with only nodes

### JVM Languages Support

The support for Java-based projects allows to statically analyze the cohesion, coupling and complexity of Java code. We map **classes** to **modules** and **methods** to **nodes** in the analysis model. In Java classes are used to represent data structures, components and a wide varienty OOP-patterns. Especially, data structures should usually be excluded from the analysis, as they do not possess relevant semantics. Furthermore, Java projects may include a wide range of libraries and frameworks which should not be added entirely to the complexity of the system, as they provide an abstraction. The same may aply to factory and utility classes. To ensure that only the relevant parts of a project are included in the complexity analysis, the part of the projects has to be specified. Therefore, two files **data-type-pattern.cfg** and **observed-system.cfg** must be created and filled with proper Java regular experessions.

The analyses requires two distinct configuration files:
1. **data-type-pattern.cfg** contains all class names which represent data types. This includes data type classes of Java, like java.lang.String. The file accepts wildcards in its pattern.
1. **observed-system.cfg** contains all class names which belong to the observed system and must be represented in the hypergraph must be listed in a similar way in a file called observed-system.cfg

**Analysis**
- Place both **data-type-pattern.cfg** and **observed-system.cfg** in the project root of a Java project in Eclipse.
- Right-click on the project in the Project Explorer and select the Complexity Analysis from the context menu
- Wait for the computation.
- The results are displayed in a analysis result view. From there you can open graphical representation of the analysis graph.

Note: Depending on the size of the project and the used framework and library functionalities, the analysis could take hours. Presently, the tool parallelizes certain operations assuming that the machine has 8 CPUs. However, this should be configurable in future.

Note: The tool will be extended to support other languages, like Xtend and Groovy to complement the Java project analysis. 

### Other Models

Presently, we support the GECO megamodel language and EMF metamodels as input for the analysis. The EMF metamodel analysis is restricted to **packages** and **classes** which are mapped to **modules** and **nodes** respectively.  


[1] Edward B. Allen, 2007; Measuring size, complexity, and coupling of hypergraph abstractions of
    software: An information-theory approach
