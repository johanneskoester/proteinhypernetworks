/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.graphs;

import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * A hypergraph with protein complexes as hyperedges.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface ProteinComplexesHypergraph {

  public void setProteinComplexes(ProteinHypernetwork hypernetwork, Collection<Complex> complexes);
}
