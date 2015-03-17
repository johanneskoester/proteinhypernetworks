# Protein Hypernetworks

Protein Hypernetworks is a framework for modelling protein networks including boolean interaction dependencies. Details can be found at http://www.rahmannlab.de/research/hypernetworks or

Johannes Köster, Eli Zamir, Sven Rahmann. Protein Hypernetworks: a Logic Framework for Interaction Dependencies and Perturbation Effects in Protein Networks; a preprint is available at arXiv: 1106.2562.

Please use above preprint for citation for now.

## Protein Hypernetworks Software Suite

This software suite consists of an editor ProteinHypernetworkEditor and a predition tool ProteinHypernetwork.

The editor allows to generate and edit protein hypernetworks in the Hypernetwork Markup Language (HypernetworkML) file format. It allows the import of protein network definitions in PSIMI 2.5 and Cytoscape SIF.

The prediction tool allows to predict protein complexes and perturbation effects. Thereby, both tools allow internal and Cytoscape visualization of results and networks.

## Used Libraries

The software suite uses LibModalLogic for propositional logic representation and reasoning. Further, the JAVA Universal Network/Graph Framework is used for graph representation and visualization. Lastly the FibonacciHeap implementation of JGraphT is used for experimental and not yet activated code. All required dependencies are included into the provided downloads.
