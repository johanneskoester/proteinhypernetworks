/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.graphs;

import edu.uci.ics.jung.graph.SetHypergraph;
import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.proteins.Protein;

/**
 * A hypergraph with protein complexes as hyperedges.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class JungProteinComplexesHypergraph extends SetHypergraph<Protein, Complex> implements proteinHypernetworkVisualization.graphs.ProteinComplexesHypergraph {

  public void setProteinComplexes(ProteinHypernetwork hypernetwork, Collection<Complex> complexes) {
    for(Complex c : complexes) {
      for(Protein p : c)
        addVertex(p);
      addEdge(c, c);
    }
  }
}
