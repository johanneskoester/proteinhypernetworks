/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.pis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import logicProteinHypernetwork.networkStates.MinimalNetworkStatesGraphs;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Processor to predict the single PIS for proteins or interactions.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class PISPrediction extends Processor {

  private int threadCount;
  private ProteinHypernetwork hypernetwork;
  private Perturbations perturbations;
  private MinimalNetworkStates mns;
  private MinimalNetworkStatesGraphs mnsg;
  protected List<PIS> pis;
  private boolean doInteractions;

  /**
   * Constructor of class PISPrediction.
   * 
   * @param lHypernetwork the logic hypernetwork instance
   * @param threadCount the number of threads
   * @param doInteractions whether interactions PIS should be predicted
   */
  public PISPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount, boolean doInteractions) {
    this.threadCount = threadCount;
    this.doInteractions = doInteractions;
    this.hypernetwork = hypernetwork;
    this.perturbations = perturbations;
  }

  /**
   * Returns the predicted PIS.
   *
   * @return the PIS
   */
  public List<? extends PIS> getPIS() {
    return pis;
  }

  public void process() {
    System.out.println("Predicting master switches...");
    calculateScores();
    System.out.println("Finished predicting master switches.");
  }

  /**
   * Calculate the scores.
   */
  private void calculateScores() {
    List<? extends NetworkEntity> entities = null;
    if (doInteractions) {
      entities = hypernetwork.getInteractions();
    } else {
      entities = hypernetwork.getProteins();
    }

    calculateScores(entities);
  }

  /**
   * Calculate the scores.
   *
   * @param entities the network entities for which the scores should be calculated
   */
  protected void calculateScores(List<? extends NetworkEntity> entities) {
    mns = new MinimalNetworkStates(hypernetwork);
    mns.listen(this);
    mns.process();
    mns.setPerturbation(perturbations.getPerturbations());

    mnsg = new MinimalNetworkStatesGraphs(mns);

    progressBean.setProgress(0);
    pis = new ArrayList();

    preparePIS(entities);

    ToDo todo = new ToDo(pis.size());
    for (final PIS ms : pis) {
      calculateScore(ms);
      todo.increaseDone();
      progressBean.setProgress(todo.getDone(), todo.getTodo());
    }
  }

  /**
   * Helper method to fill the pis attribute with a list of PIS container objects.
   *
   * @param entities the network entities
   */
  protected void preparePIS(List<? extends NetworkEntity> entities) {
    for (NetworkEntity e : entities) {
      pis.add(new SinglePIS(e));
    }
  }

  /**
   * Calculate the score for a certain PIS object.
   *
   * @param p the PIS object
   */
  protected void calculateScore(PIS p) {
    calculateScore(p, true);
  }

  /**
   * Calculate the PIS by looking at the propagation effect.
   *
   * @param p the PIS for that the score should be calculated
   */
  protected void calculateScore(PIS p, boolean setAffected) {
    Map<NetworkEntity, Integer> dist = new HashMap<NetworkEntity, Integer>();
    Deque<NetworkEntity> todo = new ArrayDeque<NetworkEntity>();

    for (NetworkEntity e : p) {
      if (mns.isPossible(e)) {
        todo.add(e);
        dist.put(e, 0);
      }
    }

    while (!todo.isEmpty()) {
      NetworkEntity f = todo.removeFirst();
      int d = dist.get(f);

      for (NetworkEntity g : mnsg.getActivationGraph().getSuccessors(f)) {
        if (!dist.containsKey(g)) {
          todo.addLast(g);

          dist.put(g, d + 1);
        }
      }
      for (NetworkEntity g : mnsg.getInhibitionGraph().getSuccessors(f)) {
        if (!dist.containsKey(g)) {
          todo.addLast(g);

          dist.put(g, d + 1);
        }
      }
    }

    int score = 0;
    for (int d : dist.values()) {
      score += d;
    }

    p.setScore(score);

    if (setAffected) {
      p.setAffected(dist.keySet());
    }
  }
}
