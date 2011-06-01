/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.graphs;

import java.util.Collection;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.proteins.Protein;

/**
 * A protein network as a graph.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface ProteinNetworkGraph {

  public void setProteinNetwork(ProteinHypernetwork hypernetwork, Collection<Protein> proteins);
}
