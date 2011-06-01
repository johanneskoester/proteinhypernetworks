/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.decoder;

import java.io.File;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Decodes a protein hypernetwork from an input file.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface Decoder {

  /**
   * Decode a protein hypernetwork.
   *
   * @param file the input file
   * @param proteinHypernetwork the resulting protein hypernetwork
   * @throws Exception an exception while decoding
   */
  public void decode(File file, ProteinHypernetwork proteinHypernetwork) throws Exception;
}
