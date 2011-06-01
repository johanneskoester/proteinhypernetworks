/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes.lcma;

import logicProteinHypernetwork.analysis.complexes.ProteinSubnetwork;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.SPINComplexRefinement;

/**
 * Refine complexes using LCMA.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class LCMAComplexRefinement extends SPINComplexRefinement {

  /**
   * Constructor of class LCMAComplexRefinement.
   * 
   * @param ppin the protein network
   * @param complexPrediction the complex prediction object
   */
  public LCMAComplexRefinement(ProteinSubnetwork ppin, SPINComplexPrediction complexPrediction) {
    super(ppin, complexPrediction);
  }

  @Override
  protected SubcomplexCollector createSubcomplexCollector() {
    return new OmitFullyContainedSubcomplexCollector();
  }

}
