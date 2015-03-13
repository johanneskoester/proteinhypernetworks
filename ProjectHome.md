# Protein Hypernetworks #
**Protein Hypernetworks** is a framework for modelling protein networks including boolean interaction dependencies. Details can be found at http://www.rahmannlab.de/research/hypernetworks or

Johannes Köster, Eli Zamir, Sven Rahmann. Protein Hypernetworks: a Logic Framework for Interaction Dependencies and Perturbation Effects in Protein Networks; a preprint is available at [arXiv: 1106.2562](http://arxiv.org/abs/1106.2562).

Please use above preprint for citation for now.

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_constraints.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_constraints.png)

# Protein Hypernetworks Software Suite #

Please see our [Installation](Installation.md) and [Usage](Usage.md) tutorials.

This software suite consists of an editor _ProteinHypernetworkEditor_ and a predition tool _ProteinHypernetwork_.

The editor allows to generate and edit protein hypernetworks in the [Hypernetwork Markup Language (HypernetworkML)](https://sites.google.com/site/rahmannlab/research/hypernetworks/hypernetworkml) file format. It allows the import of protein network definitions in [PSIMI 2.5](http://www.psidev.info/index.php?q=node/60&conversationContext=1) and [Cytoscape SIF](http://www.cytoscape.org/).

The prediction tool allows to predict **protein complexes** and **perturbation effects**.
Thereby, both tools allow internal and [Cytoscape](http://www.cytoscape.org/) visualization of results and networks.

## Used Libraries ##
The software suite uses [LibModalLogic](http://code.google.com/p/libmodallogic/) for propositional logic representation and reasoning.
Further, the [JAVA Universal Network/Graph Framework](http://jung.sourceforge.net/) is used for graph representation and visualization.
Lastly the FibonacciHeap implementation of [JGraphT](http://www.jgrapht.org/) is used for experimental and not yet activated code. All required dependencies are included into the provided downloads.