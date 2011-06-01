/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.graphs;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.Collection;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * A graph for a protein network.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class JungProteinNetworkGraph extends UndirectedSparseGraph<Protein, Interaction> implements proteinHypernetworkVisualization.graphs.ProteinNetworkGraph {

  public void setProteinNetwork(ProteinHypernetwork hypernetwork) {
    setProteinNetwork(hypernetwork, hypernetwork.getProteins());
  }

  public void setProteinNetwork(ProteinHypernetwork hypernetwork, Collection<Protein> proteins) {
    for(Protein p : proteins) {
      addVertex(p);
    }
    for(Interaction i : hypernetwork.getInteractions()) {
      addEdge(i, i.getProteins());
    }
  }
}
