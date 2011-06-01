/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes;

import org.apache.commons.collections15.Factory;

/**
 * Factory to create empty protein complexes.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexFactory implements Factory<Complex>{

  /**
   * Creates a new empty protein complex.
   *
   * @return the complex
   */
  public Complex create() {
    return new Complex();
  }

}
