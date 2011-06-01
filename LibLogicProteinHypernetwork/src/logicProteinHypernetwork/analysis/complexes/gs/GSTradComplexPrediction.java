/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes.gs;

import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import proteinHypernetwork.ProteinHypernetwork;
import util.graphSummarization.GraphSummarizationComplexPrediction;

/**
 * Graph Summarization based complex prediction with SPIN approach. Since GS is
 * a greedy algorithm that does not enumerate all complexes,
 * it is not advisable to do this kind of prediction because GS won't predict
 * alternative nearly as good Complexes without the clashing interactions.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class GSTradComplexPrediction extends SPINComplexPrediction {
  
  public GSTradComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    super(hypernetwork, perturbations, threadCount);
    this.plainComplexPrediction = new GraphSummarizationComplexPrediction();
  }
}
