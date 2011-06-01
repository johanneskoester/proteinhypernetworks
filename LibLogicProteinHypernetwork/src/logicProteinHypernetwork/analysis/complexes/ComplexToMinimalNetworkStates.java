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
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.interactions.Interaction;

/**
 * Transforms complexes to sets of minimal network states.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexToMinimalNetworkStates implements Transformer<Complex, Collection<MinimalNetworkState>> {

  private ProteinSubnetwork ppin;
  private MinimalNetworkStates minimalNetworkStates;

  /**
   * Constructor of class ComplexToMinimalNetworkStates.
   * @param ppin the protein network
   * @param minimalNetworkStates the minimal network states
   */
  public ComplexToMinimalNetworkStates(ProteinSubnetwork ppin, MinimalNetworkStates minimalNetworkStates) {
    this.ppin = ppin;
    this.minimalNetworkStates = minimalNetworkStates;
  }

  /**
   * Returns all minimal network states belonging to given complex.
   * 
   * @param c the complex
   * @return the minimal network states
   */
  public Collection<MinimalNetworkState> transform(Complex c) {
    ProteinSubnetwork naiveComplex = FilterUtils.createInducedSubgraph(c, ppin);

    Collection<MinimalNetworkState> mns = new ArrayList<MinimalNetworkState>();

    for (Interaction i : naiveComplex.getEdges()) {
      try {
        mns.addAll(minimalNetworkStates.getMinimalNetworkStates(i));
      } catch (NullPointerException ex) {
        // happens if artificially inserted interactions occur. These do not hurt since they won't have any competitors.
      }
    }

    return mns;
  }
}
