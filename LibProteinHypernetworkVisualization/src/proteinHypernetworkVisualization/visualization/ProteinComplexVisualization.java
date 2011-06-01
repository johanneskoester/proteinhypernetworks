/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.visualization;

import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
import proteinHypernetwork.ProteinHypernetwork;


/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface ProteinComplexVisualization extends Visualization {

  public void setProteinComplexes(ProteinHypernetwork hypernetwork, Collection<Complex> complexes);
}
