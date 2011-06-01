/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.visualization;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface JungVisualization {

  public void saveToPNG(File file) throws IOException;
  public void saveToGraphML(File file) throws IOException;
}
