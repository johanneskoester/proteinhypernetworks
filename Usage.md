# Tutorial #

We describe an example workflow for the Protein Hypernetwork Software Suite.

## ProteinHypernetworkEditor ##

The ProteinHypernetworkEditor allows to load, edit and save protein hypernetworks. The main interface consists of editors for proteins, interactions and constraints, accessible through the left panel:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor.png)

In each editor, entities are shown in a vertical list. **+** and **-** buttons allow to add or remove entities. Finally, the **Search...** field allows to search the list for items containing a given sequence of letters.

Further, it is possible to import existing networks into the system. This can be done by opening the **File** menu:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_import.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_import.png)

Here, we import an existing network given as a Cytoscape .sif file.
The loaded network can be visualized by clicking on **Visualize** on the left panel:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_visualize.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_visualize.png)

Now, we can add and edit constraints to the network by clicking on **Editor** in the **Constraints** section of the left panel:


![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_constraints.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_constraints.png)

Finally the hypernetwork can be saved in the HypernetworkML format by opening the **File** menu again:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_save.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetworkEditor/tutorial/editor_save.png)

## ProteinHypernetwork ##

The second package of the software suite, ProteinHypernetwork, allows to make predictions on a given protein hypernetwork. For now, this includes the prediction of protein complexes and the prediction of the perturbation impact score i.e. its functional importance in the network (Köster et al. 2011; [arXiv: 1106.2562](http://arxiv.org/abs/1106.2562)).

In the main interface, the left panel makes the provided predictions accessible. The right side of the window shows the results of a prediction:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator.png)

We start by loading the hypernetwork we created above using the **File** menu:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_open.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_open.png)

Once a hypernetwork is loaded, one can make a prediction using the left panel. The result of the complex prediction on this network is displayed on the right as a list of complexes:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_complexes.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_complexes.png)

Predicted complexes can be visualized, e.g. using colored hyperedges:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_visualization.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_visualization.png)

When predicting the perturbation impact score, on the right a list with the score for every protein is displayed. Checkboxes in the left panel allow to predict the score for interactions or synthetic perturbations:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_pis.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_pis.png)

All predictions can be exported using the **File** menu:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_export.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_export.png)

Finally, it is possible to perform any prediction under a given a priori perturbation. This can be configured with the perturbation editor that is invoked using the left panel:

![http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_perturbation.png](http://proteinhypernetworks.googlecode.com/svn/trunk/ProteinHypernetwork/tutorial/simulator_perturbation.png)