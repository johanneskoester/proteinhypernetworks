/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes.offline;

import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Predict and refine complexes with external command.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class OfflineComplexPrediction extends SPINComplexPrediction {

  /**
   * Constructor of class OfflineComplexPrediction.
   *
   * @param complexPredictionCommand the external command
   * @param hypernetwork the hypernetwork
   * @param perturbations the perturbations
   * @param threadCount the number of threads
   */
  public OfflineComplexPrediction(String complexPredictionCommand, ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    super(hypernetwork, perturbations, threadCount);
    this.plainComplexPrediction = new ComplexPredictionCommand(complexPredictionCommand, hypernetwork);
  }
}
