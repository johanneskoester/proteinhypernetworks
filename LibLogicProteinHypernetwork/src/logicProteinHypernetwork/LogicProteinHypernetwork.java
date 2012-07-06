/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.lcma.LCMAComplexPrediction;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarityOutputStream;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarityPrediction;
import logicProteinHypernetwork.analysis.pis.PIS;
import logicProteinHypernetwork.analysis.pis.PISPrediction;
import logicProteinHypernetwork.analysis.pis.SinglePIS;
import logicProteinHypernetwork.analysis.pis.SyntheticPISPrediction;
import logicProteinHypernetwork.analysis.reactions.Reaction;
import logicProteinHypernetwork.analysis.reactions.ReactionsPrediction;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.proteins.Protein;
import util.ProgressBean;

/**
 * Class LogicProteinHypernetwork defines the entry point of all provided
 * hypernetwork analyses.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class LogicProteinHypernetwork {

  private ProteinHypernetwork proteinHypernetwork;
  private Perturbations perturbations;
  private SPINComplexPrediction complexPrediction;
  private static Class<? extends SPINComplexPrediction> complexPredictionClass = LCMAComplexPrediction.class;
  private PISPrediction pisPrediction;
  private FunctionalSimilarityPrediction functionalSimilarityPrediction;
  private ReactionsPrediction reactionsPrediction;
  private ProgressBean progressBean = new ProgressBean();
  private int threadCount;
  private boolean pisDoSynthetic;
  private boolean pisDoInteractions;

  /**
   * Constructor of class ModalLogicProteinHypernetwork.
   *
   * @param h the hypernetwork
   * @param threadCount the number of used threads
   */
  public LogicProteinHypernetwork(ProteinHypernetwork h, int threadCount) {
    proteinHypernetwork = h;
    this.threadCount = threadCount;
    perturbations = new Perturbations();
  }

  /**
   * Returns the number of used threads.
   *
   * @return the number of used threads
   */
  public int getThreadCount() {
    return threadCount;
  }

  /**
   * Set whether next PIS prediction should consider synthetic interactions.
   *
   * @param synthetic whether next PIS prediction should consider synthetic interactions
   */
  public void setPISDoSynthetic(boolean synthetic) {
    pisDoSynthetic = synthetic;
  }

  /**
   * Set whether next PIS prediction should consider interactions.
   *
   * @param interactions whether next PIS prediction should consider interactions
   */
  public void setPISDoInteractions(boolean interactions) {
    pisDoInteractions = interactions;
  }

  /**
   * Set protein complex prediction algorithm.
   *
   * @param complexPrediction the protein complex prediction algorithm
   */
  public static void setProteinComplexPrediction(Class<? extends SPINComplexPrediction> complexPrediction) {
    complexPredictionClass = complexPrediction;
  }

  /**
   * Returns the protein hypernetwork.
   *
   * @return the protein hypernetwork
   */
  public ProteinHypernetwork getProteinHypernetwork() {
    return proteinHypernetwork;
  }

  /**
   * Returns the progress indicator bean.
   *
   * @return the progress indicator bean
   */
  public ProgressBean getProgressBean() {
    return progressBean;
  }

  /**
   * Creates a complex prediction object.
   *
   * @param perturbations the perturbations
   * @param threadCount the number of threads
   * @return the complex prediction object
   * @throws InstantiationException if complex prediction object could not be created
   */
  private SPINComplexPrediction createComplexPrediction(Perturbations perturbations, int threadCount) throws InstantiationException {
    try {
      Constructor<? extends SPINComplexPrediction> constructor = complexPredictionClass.getConstructor(ProteinHypernetwork.class, Perturbations.class, int.class);
      return constructor.newInstance(proteinHypernetwork, perturbations, threadCount);
    } catch (Exception ex) {
      throw new InstantiationException(ex.getMessage());
    }
  }

  /**
   * Predict protein complexes.
   *
   * @throws InstantiationException if complex prediction object could not be created
   */
  public void predictComplexes() throws InstantiationException {
    complexPrediction = createComplexPrediction(perturbations, threadCount);
    complexPrediction.getProgressBean().addPropertyChangeListener(progressBean);
    complexPrediction.process();
  }

  /**
   * Predict PIS.
   */
  public void predictPIS() {
    if (pisDoSynthetic) {
      pisPrediction = new SyntheticPISPrediction(proteinHypernetwork, perturbations, threadCount, pisDoInteractions);
    } else {
      pisPrediction = new PISPrediction(proteinHypernetwork, perturbations, threadCount, pisDoInteractions);
    }
    pisPrediction.getProgressBean().addPropertyChangeListener(progressBean);
    pisPrediction.process();
  }

  /**
   * Predict functional similarities.
   * 
   * @param os the output stream
   * @throws IOException if output failed
   */
  public void predictFunctionalSimilarities(FunctionalSimilarityOutputStream os) throws IOException {
    functionalSimilarityPrediction = new FunctionalSimilarityPrediction(this.proteinHypernetwork, getSinglePIS());
    functionalSimilarityPrediction.setOutputStream(os);
    functionalSimilarityPrediction.getProgressBean().addPropertyChangeListener(progressBean);
    functionalSimilarityPrediction.process();
  }
  
  public void predictReactions(Collection<Protein> proteins) {
    reactionsPrediction = new ReactionsPrediction(proteinHypernetwork, 5, 100);
    reactionsPrediction.setProteins(proteins);
    reactionsPrediction.getProgressBean().addPropertyChangeListener(progressBean);
    reactionsPrediction.process();
  }
  
  public List<Reaction> getReactions() {
    return reactionsPrediction.getReactions();
  }

  /**
   * Returns predicted complexes.
   *
   * @return the predicted complexes
   */
  public Collection<Complex> getComplexes() {
    return complexPrediction.getComplexes();
  }

  /**
   * Returns predicted PIS.
   *
   * @return the PIS
   */
  public List<? extends PIS> getPIS() {
    return pisPrediction.getPIS();
  }

  /**
   * Returns predicted PIS as list of SinglePIS objects.
   *
   * @return the PIS
   */
  public List<SinglePIS> getSinglePIS() {
    if (pisPrediction instanceof PISPrediction) {
      return (List<SinglePIS>) pisPrediction.getPIS();
    }
    throw new IllegalStateException("Synthetic master switches were predicted.");
  }

  /**
   * Perturb a network entity.
   *
   * @param e the network entity
   */
  public void perturbation(NetworkEntity e) {
    perturbations.perturbation(e);
  }

  /**
   * Undo the perturbation of a network entity.
   *
   * @param e the network entity
   */
  public void undoPerturbation(NetworkEntity e) {
    perturbations.undoPerturbation(e);
  }

  /**
   * Returns the perturbations.
   *
   * @return the perturbated network entities
   */
  public Collection<NetworkEntity> getPerturbations() {
    return perturbations.getPerturbations();
  }
}
