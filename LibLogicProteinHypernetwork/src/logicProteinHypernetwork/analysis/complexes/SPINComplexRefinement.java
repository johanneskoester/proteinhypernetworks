/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes;

import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.minimalNetworkStatesToNetworkStates.MinimalNetworkStatesToNetworkStates;
import logicProteinHypernetwork.networkStates.NetworkState;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Refine complexes based on the SPIN approach.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class SPINComplexRefinement implements Transformer<Complex, Collection<Complex>> {

  protected SPINComplexPrediction complexPrediction;
  protected ProteinSubnetwork ppin;
  protected NetworkStateToSubnetwork networkStateToSubgraph = new NetworkStateToSubnetwork();
  protected ComplexHandler complexHandler = new ComplexHandler();

  /**
   * Constructor of class SPINComplexRefinement.
   *
   * @param ppin the protein network
   * @param complexPrediction the complex prediction object
   */
  public SPINComplexRefinement(ProteinSubnetwork ppin, SPINComplexPrediction complexPrediction) {
    this.complexPrediction = complexPrediction;
    this.ppin = ppin;
  }

  /**
   * Returns a refined complexes for a given naively predicted complex.
   *
   * @param complex the complex
   * @return the refined complexes
   */
  public Collection<Complex> transform(Complex complex) {
    Collection<MinimalNetworkState> mns = complexPrediction.getMinimalNetworkStatesMap().get(complex);
    Iterator<NetworkState> states = new MinimalNetworkStatesToNetworkStates().transform(mns);

    SubcomplexCollector subcomplexCollector = createSubcomplexCollector();
    while (states.hasNext()) {
      subcomplexCollector.setNextState();
      NetworkState s = states.next();
      ProteinSubnetwork spin = networkStateToSubgraph.transform(s);

      handleSPIN(complex, s, spin, subcomplexCollector);
    }
    return subcomplexCollector.getComplexes();
  }

  /**
   * Handle the SPIN.
   *
   * @param complex the complex
   * @param s the network state
   * @param spin the protein subnetwork
   * @param subcomplexCollector the subcomplex collector
   */
  protected void handleSPIN(Complex complex, NetworkState s, ProteinSubnetwork spin, SubcomplexCollector subcomplexCollector) {
    Collection<Complex> simultaneousComplexes = getSubcomplexes(complex, spin);

      for (Complex sc : simultaneousComplexes) {

        // add all necessary interactions
        ProteinSubnetwork cin = FilterUtils.createInducedSubgraph(sc, spin);

        MinimalNetworkStateInvestigator mi = null;
        mi = new MinimalNetworkStateInvestigator();

        for (MinimalNetworkState m : s.getMinimalNetworkStates()) {
          mi.investigate(m, cin);
        }

        Complex c = new Complex(cin.getVertices());
        c.setSubnetwork(cin);

        subcomplexCollector.addComplex(c);
      }
  }

  /**
   * Naively predict subcomplexes on the protein subnetwork for given complex.
   *
   * @param c the complex
   * @param s the protein subnetwork
   * @return the predicted subcomplexes
   */
  protected Collection<Complex> getSubcomplexes(Complex c, ProteinSubnetwork s) {
    return complexPrediction.getPlainComplexPrediction().transform(s);
  }

  /**
   * Create the subcomplex collector.
   * @return the subcomplex collector
   */
  protected SubcomplexCollector createSubcomplexCollector() {
    return new DefaultSubcomplexCollector();
  }

  /**
   * Provides interface to a heuristic that collects the subcomplexes that should go
   * into the prediction.
   */
  protected interface SubcomplexCollector {
    public void addComplex(Complex c);
    public void setNextState();
    public Collection<Complex> getComplexes();
  }

  /**
   * Collect all subcomplexes.
   */
  protected class DefaultSubcomplexCollector implements SubcomplexCollector {

    private Collection<Complex> complexes = new HashSet<Complex>();

    public void addComplex(Complex c) {
      complexes.add(c);
    }

    public void setNextState() {
      
    }

    public Collection<Complex> getComplexes() {
      return complexes;
    }
  }

  /**
   * Omit subcomplexes that are fully contained in others.
   */
  public class OmitFullyContainedSubcomplexCollector implements SubcomplexCollector {
    private Collection<Complex> complexes = new HashSet<Complex>();
    private Collection<Complex> currentComplexes;

    public void addComplex(Complex c) {
      Iterator<Complex> ite = currentComplexes.iterator();
      while(ite.hasNext()) {
        Complex cu = ite.next();
        if(CollectionUtils.isSubCollection(cu, c)) {
          ite.remove(); // remove all complexes that are subsets of the new one
        }
        else {
          if(CollectionUtils.isSubCollection(c, cu)) {
            return; // don't add the new complex if it is subset of any existing
          }
        }
      }
      currentComplexes.add(c);
    }

    public void setNextState() {
      if(currentComplexes != null)
        complexes.addAll(currentComplexes);
      currentComplexes = new ArrayList<Complex>();
    }

    public Collection<Complex> getComplexes() {
      setNextState();
      return complexes;
    }

  }

  /**
   * Take the smallest set of subcomplexes for each SPIN.
   */
  public class FewestSubcomplexCollector implements SubcomplexCollector {
    private Collection<Complex> fewestComplexes;
    private Collection<Complex> currentComplexes;

    public void addComplex(Complex c) {
      currentComplexes.add(c);
    }

    public void setNextState() {
      if(currentComplexes != null) {

        if(fewestComplexes == null || currentComplexes.size() < fewestComplexes.size()) {
          fewestComplexes = currentComplexes;
        }
      }
      currentComplexes = new HashSet<Complex>();
    }

    public Collection<Complex> getComplexes() {
      if(fewestComplexes == null)
        return currentComplexes;
      else {
        setNextState();
        return fewestComplexes;
      }
    }


  }

  /**
   * Take the smallest subcomplexes for each SPIN.
   */
  public class SmallestSubcomplexCollector implements SubcomplexCollector {
    private Collection<Complex> smallestComplexes;
    private Collection<Complex> currentComplexes;
    private float avgSize;

    public void addComplex(Complex c) {
      currentComplexes.add(c);
    }

    public void setNextState() {
      if(currentComplexes != null) {
        float avg = 0;
        for(Complex c : currentComplexes) {
          avg += c.size();
        }
        avg /= currentComplexes.size();

        if(smallestComplexes == null || avgSize > avg) {
          smallestComplexes = currentComplexes;
          avgSize = avg;
        }
      }
      currentComplexes = new HashSet<Complex>();
    }

    public Collection<Complex> getComplexes() {
      if(smallestComplexes == null)
        return currentComplexes;
      else {
        setNextState();
        return smallestComplexes;
      }
    }


  }

  /**
   * Take the most dense subcomplexes for each SPIN.
   */
  public class DensestSubcomplexCollector implements SubcomplexCollector {

    private float density = 0;
    private Collection<Complex> densestComplexes;
    private Collection<Complex> currentComplexes;


    public void addComplex(Complex c) {
      currentComplexes.add(c);
    }

    public void setNextState() {
      if(currentComplexes != null) {
        float newDensity = 0;
        for(Complex c : currentComplexes) {
          newDensity += c.getSubnetwork().getDensity();
        }
        newDensity /= currentComplexes.size();
        if(newDensity > density) {
          density = newDensity;
          densestComplexes = currentComplexes;
        }
      }
      currentComplexes = new HashSet<Complex>();
    }

    public Collection<Complex> getComplexes() {
      if(densestComplexes == null)
        return currentComplexes;
      else {
        setNextState();
        return densestComplexes;
      }
    }

  }

  private class MinimalNetworkStateInvestigator {

    void investigate(MinimalNetworkState m, ProteinSubnetwork cin) {
      NetworkEntity q = m.getEntity();

      if ((q instanceof Protein && cin.containsVertex((Protein)q)) ||
              (q instanceof Interaction && cin.containsEdge((Interaction)q))) {
        for (NetworkEntity e : m) {
          if (e instanceof Interaction) {
            Interaction i = (Interaction) e;
            for (Protein p : i.getProteins()) {
              cin.addVertex(p);
            }
            cin.addEdge(i, i.getProteins());
          }
        }
      }
    }
  }

  protected class ComplexHandler {
    public void handle(Complex complex, ProteinSubnetwork cin) {
    }
  }
}
