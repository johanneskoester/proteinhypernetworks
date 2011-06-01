/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes.gs;

import java.util.ArrayList;
import java.util.Collection;
import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.NetworkEntitiesToSubnetwork;
import logicProteinHypernetwork.analysis.complexes.ProteinSubnetwork;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;
import util.graphSummarization.GraphSummarization;

/**
 * Predict complexes using Graph Summarization.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class GSComplexPrediction extends Processor {

  private MinimalNetworkStates minimalNetworkStates;
  private ProteinHypernetwork hypernetwork;
  private Perturbations perturbations;
  private int threadCount;
  private Collection<Complex> complexes;

  /**
   * Constructor of class GSComplexPrediction.
   *
   * @param hypernetwork the hypernetwork
   * @param perturbations the perturbations
   * @param threadCount the number of threads
   */
  public GSComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    this.hypernetwork = hypernetwork;
    this.perturbations = perturbations;
    this.threadCount = threadCount;
  }

  /**
   * Returns the predicted complexes
   *
   * @return the complexes
   */
  public Collection<Complex> getComplexes() {
    return complexes;
  }

  /**
   * Process the complex prediction
   */
  @Override
  public void process() {
    minimalNetworkStates = new MinimalNetworkStates(hypernetwork);
    minimalNetworkStates.listen(this);
    minimalNetworkStates.process();

    minimalNetworkStates.setPerturbation(perturbations.getPerturbations());

    ProteinSubnetwork ppin = new NetworkEntitiesToSubnetwork().transform(minimalNetworkStates.getPossibleEntities());
    for (Protein p : ppin.getVertices()) {
      if (!ppin.isNeighbor(p, p)) {
        Interaction i = new Interaction();
        i.addInteractor(new Interactor(p));
        i.addInteractor(new Interactor(p));
        ppin.addEdge(i, i.getProteins());
      }
    }

    GraphSummarization<Protein, Interaction> gs =
            new GraphSummarization<Protein, Interaction>(new ConstrainedSupervertexCost(minimalNetworkStates));
    gs.getProgressBean().addPropertyChangeListener(progressBean);
    Collection<? extends Iterable<Protein>> summary = gs.graphSummarization(ppin);

    complexes = new ArrayList<Complex>();
    for (Iterable<Protein> s : summary) {
      Complex c = new Complex();
      for (Protein p : s) {
        c.add(p);
      }

      complexes.add(c);
    }
  }
}
