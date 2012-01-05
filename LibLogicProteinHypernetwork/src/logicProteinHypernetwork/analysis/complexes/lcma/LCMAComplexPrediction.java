/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes.lcma;

import java.util.Collection;
import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.ComplexFactory;
import logicProteinHypernetwork.analysis.complexes.NetworkEntitiesToSubnetwork;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import util.lcma.FastLCMA;

/**
 * Predict complexes with LCMA.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class LCMAComplexPrediction extends SPINComplexPrediction {

  private static float mergeSimilarityThreshold = 0.4f;

  /**
   * Constructor of class LCMAComplexPrediction.
   *
   * @param hypernetwork the hypernetwork
   * @param perturbations the perturbations
   * @param threadCount the number of threads
   */
  public LCMAComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    super(hypernetwork, perturbations, threadCount);
    this.plainComplexPrediction = new FastLCMA<Protein, Interaction, Complex>(mergeSimilarityThreshold, new ComplexFactory());
    this.entitiesToSubgraph = new NetworkEntitiesToSubnetwork(ppin, true, false);
  }

  /**
   * Set the merge similarity threshold.
   *
   * @param mergeSimilarityThreshold the merge similarity threshold
   */
  public static void setMergeSimilarityThreshold(float mergeSimilarityThreshold) {
    LCMAComplexPrediction.mergeSimilarityThreshold = mergeSimilarityThreshold;
  }

  @Override
  public void naivePrediction() {
    if (plainComplexPrediction instanceof FastLCMA) {
      ((FastLCMA) plainComplexPrediction).setAdditionalIteration(0);
    }
    super.naivePrediction();
  }

  @Override
  public void refinement(Collection<Complex> naiveComplexes) {
    if (plainComplexPrediction instanceof FastLCMA) {
      ((FastLCMA) plainComplexPrediction).setAdditionalIteration(6);
    }
    super.refinement(naiveComplexes, new LCMAComplexRefinement(ppin, this));
  }


}
