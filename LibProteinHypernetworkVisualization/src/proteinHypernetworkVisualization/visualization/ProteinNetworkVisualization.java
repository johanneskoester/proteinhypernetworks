/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.visualization;

import proteinHypernetworkVisualization.graphs.ProteinNetworkGraph;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface ProteinNetworkVisualization extends Visualization {

  public void setProteinNetworkGraph(ProteinNetworkGraph graph);
}
