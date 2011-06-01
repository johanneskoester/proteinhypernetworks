/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * This class handles the pipeline of complex prediction.
 * It has to be specialized for each desired plain network based prediction algorithm.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class SPINComplexPrediction extends Processor {

  private NetworkEntitiesToSubnetwork entitiesToSubgraph = new NetworkEntitiesToSubnetwork();
  protected Transformer<UndirectedGraph<Protein, Interaction>, Collection<Complex>> plainComplexPrediction;
  private ProteinHypernetwork hypernetwork;
  private Collection<Complex> complexes = new Vector<Complex>(1000);
  private Collection<Complex> naiveComplexes = new Vector<Complex>(1000);
  private int threadCount;
  protected ProteinSubnetwork ppin;
  private MinimalNetworkStates minimalNetworkStates;
  private final Map<Complex, Collection<Complex>> refinementMap = new HashMap<Complex, Collection<Complex>>();
  private Perturbations perturbations;
  private Map<Complex, Collection<MinimalNetworkState>> minimalNetworkStatesMap;

  /**
   * Constructor of class ComplexPrediction
   *
   * @param hypernetwork the protein hypernetwork
   * @param perturbations the perturbations
   * @param mergeSimilarityThreshold deprecated
   * @param threadCount the number of allowed parallel threads
   */
  public SPINComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    this.hypernetwork = hypernetwork;
    this.threadCount = threadCount;

    this.perturbations = perturbations;
  }

  /**
   * Returns the used plain network based complex prediction algorithm.
   * @return the algorithm as a transformer
   */
  public Transformer<UndirectedGraph<Protein, Interaction>, Collection<Complex>> getPlainComplexPrediction() {
    return plainComplexPrediction;
  }

  /**
   * Return the predicted protein complexes.
   *
   * @return the complexes
   */
  public Collection<Complex> getComplexes() {
    return complexes;
  }

  /**
   * Returns the protein complexes predicted on the plain network.
   *
   * @return the complexes
   */
  public Collection<Complex> getNaiveComplexes() {
    return naiveComplexes;
  }

  /**
   * Sets the naive complexes.
   * 
   * @param complexes the naive complexes
   */
  public void setNaiveComplexes(Collection<Complex> complexes) {
    naiveComplexes = complexes;
  }

  /**
   * Returns a map that maps each plain network based complex to its minimal network states
   * 
   * @return the map
   */
  public Map<Complex, Collection<MinimalNetworkState>> getMinimalNetworkStatesMap() {
    return minimalNetworkStatesMap;
  }

  /**
   * Returns the refined complexes for a given plain network based complex.
   *
   * @param naiveComplex the plain network based complex
   * @return the refined complexes
   */
  public Collection<Complex> getRefined(Complex naiveComplex) {
    Collection<Complex> refined = refinementMap.get(naiveComplex);
    if (refined == null) {
      refined = new ArrayList<Complex>();
    }
    return refined;
  }

  /**
   * Process the complex prediction.
   */
  public void process() {
    System.out.println("Predicting complexes...");
    calculateMinimalNetworkStates();
    prepare();
    predict();
    System.out.println("Finished predicting complexes.");
  }

  public void calculateMinimalNetworkStates() {
    minimalNetworkStates = new MinimalNetworkStates(hypernetwork);
    minimalNetworkStates.listen(this);
    minimalNetworkStates.process();
  }

  /**
   * Prepare for the hypernetwork based complex refinement.
   */
  public void prepare() {
    possibleEntities();
    naivePrediction();
    mapNaiveToMinimalNetworkStates();

    //System.out.println("Predicted " + naiveComplexes.size() + " naive complexes");
  }

  /**
   * Actually predict the refined complexes with the protein hypernetwork.
   */
  public void predict() {
    refinement(naiveComplexes);
  }

  /**
   * Calculate minimal network states and possible network
   */
  public void possibleEntities() {
    minimalNetworkStates.setPerturbation(perturbations.getPerturbations());

    ppin = new NetworkEntitiesToSubnetwork().transform(minimalNetworkStates.getPossibleEntities());
  }

  /**
   * Perform plain network based complex prediction.
   */
  public void naivePrediction() {
    naiveComplexes = new Vector<Complex>(plainComplexPrediction.transform(ppin));
    /*for(int i = 0; i<naiveComplexes.size(); i++) {
    for(int j = i+1; j<naiveComplexes.size(); j++) {
    if(((List)naiveComplexes).get(i).equals(((List)naiveComplexes).get(j)))
    System.out.println(((List)naiveComplexes).get(i) + "   " + ((List)naiveComplexes).get(j));
    }
    }*/
  }

  /**
   * Map the plain network based protein complexes to the corresponding minimal network states.
   */
  public void mapNaiveToMinimalNetworkStates() {
    ComplexToMinimalNetworkStates complexToMinimalNetworkStates = new ComplexToMinimalNetworkStates(ppin, minimalNetworkStates);
    minimalNetworkStatesMap = new HashMap<Complex, Collection<MinimalNetworkState>>();
    for (Complex c : naiveComplexes) {
      minimalNetworkStatesMap.put(c, complexToMinimalNetworkStates.transform(c));
    }
  }

  /**
   * Refine plain network based predicted complexes based on the protein hypernetwork.
   *
   * @param naiveComplexes the plain network based complexes
   */
  public void refinement(final Collection<Complex> naiveComplexes) {
    refinement(naiveComplexes, new SPINComplexRefinement(ppin, this));
  }

  /**
   * Refine plain network based predicted complexes based on the protein 
   * hypernetwork with a certain ComplexRefinement instance.
   * 
   * @param naiveComplexes the plain network based complexes
   * @param complexRefinement the instance of ComplexRefinement
   */
  protected void refinement(final Collection<Complex> naiveComplexes, final SPINComplexRefinement complexRefinement) {

    ThreadPoolExecutor threads = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

    for (final Complex c : naiveComplexes) {
      threads.submit(new RefineComplex(c, complexRefinement));
    }

    threads.shutdown();
    try {
      threads.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (InterruptedException ex) {
      System.err.println(ex);
    }

    Set<Complex> unique = new HashSet<Complex>(complexes);
    complexes.clear();
    complexes.addAll(unique);

    //System.out.println("Refined to " + complexes.size() + " complexes");
  }

  /**
   * Runnable that allows the refinement of a complex in a separate thread.
   */
  protected class RefineComplex implements Runnable {

    private Complex c;
    private SPINComplexRefinement complexRefinement;

    /**
     * Constructor of the runnable
     *
     * @param c the plain network based complex to refine
     * @param complexRefinement the instance of ComplexRefinement
     */
    public RefineComplex(Complex c, SPINComplexRefinement complexRefinement) {
      this.c = c;
      this.complexRefinement = complexRefinement;
    }

    /**
     * Runs the thread.
     */
    public void run() {
      try {
        Collection<Complex> refined = complexRefinement.transform(c);
        synchronized (refinementMap) {
          if(!refinementMap.containsKey(c)) {
            refinementMap.put(c, refined);
          }
          else {
            System.err.println("Error: naive complexes not unique!");
          }
        }
        complexes.addAll(refined);
        progressBean.setProgress(refinementMap.size(), naiveComplexes.size());
        //System.out.println("Refined " + refinementMap.size() + " of " + naiveComplexes.size() + " complexes.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
