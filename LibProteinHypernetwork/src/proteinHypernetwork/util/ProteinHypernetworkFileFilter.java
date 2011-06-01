/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filters for .hml files.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinHypernetworkFileFilter extends FileFilter {

  @Override
  public boolean accept(File f) {
    return f.isDirectory() || f.getName().endsWith(".hml");
  }

  @Override
  public String getDescription() {
    return "Hypernetwork Markup Language, HypernetworkML (.hml)";
  }
}
