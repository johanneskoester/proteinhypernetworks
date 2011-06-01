/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.encoder;

import java.io.File;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Encodes a protein hypernetwork into an output file.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface Encoder {

  /**
   * Encodes a protein hypernetwork into an output file.
   *
   * @param proteinHypernetwork the protein hypernetwork
   * @param file the output file
   * @throws Exception an exception that occurs during encoding
   */
  public void encode(ProteinHypernetwork proteinHypernetwork, File file) throws Exception;
}
