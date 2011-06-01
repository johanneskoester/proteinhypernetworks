/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.pis;

import java.util.List;
import logicProteinHypernetwork.Perturbations;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Processor to predict the pairwise synthetic PIS for proteins or interactions.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class SyntheticPISPrediction extends PISPrediction {

  /**
   * Constructor of class SyntheticPISPrediction
   * 
   * @param hypernetwork the protein hypernetwork
   * @param perturbations the a priori perturbations
   * @param threadCount the number of threads
   * @param doInteractions whether interactions should be taken into account
   */
  public SyntheticPISPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount, boolean doInteractions) {
    super(hypernetwork, perturbations, threadCount, doInteractions);
  }

  @Override
  protected void preparePIS(List<? extends NetworkEntity> entities) {
    for (int i = 0; i < entities.size(); i++) {
      for (int j = i + 1; j < entities.size(); j++) {
        pis.add(new MultiPIS(entities.get(i), entities.get(j)));
      }
    }
  }

  @Override
  protected void calculateScore(PIS ms) {
    calculateScore(ms, false);
  }
}
