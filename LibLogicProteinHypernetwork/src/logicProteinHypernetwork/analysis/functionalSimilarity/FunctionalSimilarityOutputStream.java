/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.functionalSimilarity;

import java.io.BufferedWriter;
import java.io.IOException;


/**
 * Output stream for functional similarities.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FunctionalSimilarityOutputStream {
  private BufferedWriter os;

  /**
   * Constructor of class FunctionalSimilarityOutputStream.
   * 
   * @param os the output stream
   */
  public FunctionalSimilarityOutputStream(BufferedWriter os) {
    this.os = os;
  }

  /**
   * Write a similarity into the output stream.
   *
   * @param fs the similarity
   * @throws IOException if writing fails
   */
  public void write(FunctionalSimilarity fs) throws IOException {
    os.append(fs.toString());
    os.append("\n");
  }
}
