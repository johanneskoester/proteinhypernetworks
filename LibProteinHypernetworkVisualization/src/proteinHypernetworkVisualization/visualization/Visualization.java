/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.visualization;

import javax.swing.JPanel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface Visualization {

  public void visualize();
  public JPanel getVisualization();
}
