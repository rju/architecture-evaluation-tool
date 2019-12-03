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

The **architecture evaluation tool** uses maven as build system. To compile the plugins
* git clone https://github.com/rju/architecture-evaluation-tool.git
* mvn compile
* mvn package (currently you need to modify the pom.xml in de.cau.cs.se.software.evaluation.repository to be able to deploy the repository to your own location)
* Add the repository location to your eclipse update sites and install the tool

## Using Eclipse to build

* Import project as maven projects
* Set API-baseline
* Required plugins for Eclipse which you should install in advance
  ** [PCM v4.0](https://sdqweb.ipd.kit.edu/wiki/PCM_Installation) is necessary for the PCM evaluation part
  ** [Geco](https://maui.se.informatik.uni-kiel.de/repo/geco/release/1.0.0 or https://maui.se.informatik.uni-kiel.de/repo/geco/snapshot) provides basic infrastructure for all transformations
  ** [Klighd](http://rtsys.informatik.uni-kiel.de/~kieler/updatesite/nightly-openkieler/) (Without Ptolemy) used for graph visualization
* Trigger clean and build 

## JVM Languages Support

The tool will be extended to support other languages, like Xtend and Groovy to complement the Java project analysis. For the analyses the tool requires two distinct inputs:
a) Classes which represent data types must be mentioned in a data-type-pattern.cfg file. This includes data type
   classes of Java, like java.lang.String. The file accepts wildcards in its pattern.
b) Classes which belong to the observed system and must be represented in the hypergraph must be listed in a 
   similar way in a file called observed-system.cfg

When both files are in place, the user can select a Java project in Eclipse. Depending on the size of the project
and the used framework and library functionalities, the analysis could take hours. Presently, the tool parallelizes
certain operations assuming that the machine has 8 CPUs. However, this should be configurable in future.

## Other Models

Presently, only the GECO megamodel language is supported as input for the analysis. However, we intend to add a set of transformations for EMF/ECORE metamodels.

[1] Edward B. Allen, 2007; Measuring size, complexity, and coupling of hypergraph abstractions of
    software: An information-theory approach
