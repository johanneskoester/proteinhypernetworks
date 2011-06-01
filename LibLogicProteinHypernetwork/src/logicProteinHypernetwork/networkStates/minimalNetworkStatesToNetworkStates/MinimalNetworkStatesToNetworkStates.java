/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.networkStates.minimalNetworkStatesToNetworkStates;

import java.util.Collection;
import java.util.Iterator;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.NetworkState;
import org.apache.commons.collections15.Transformer;

/**
 * Transforms minimal network states to maximal combinations of not clashing minimal
 * network states in the form of network states.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MinimalNetworkStatesToNetworkStates implements Transformer<Collection<MinimalNetworkState>, Iterator<NetworkState>> {

  /**
   * Transforms minimal network states to maximal combinations of not clashing minimal
   * network states in the form of network states.
   *
   * @param mns the minimal network states
   * @return the network states
   */
  public Iterator<NetworkState> transform(Collection<MinimalNetworkState> mns) {
    MinimalNetworkState[] states = new MinimalNetworkState[mns.size()];
    mns.toArray(states);

    InstructionTree t = new InstructionTree(states);

    return t.getNetworkStateIterator();
  }

}
