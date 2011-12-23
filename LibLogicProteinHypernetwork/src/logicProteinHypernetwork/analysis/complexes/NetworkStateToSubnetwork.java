/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes;

import logicProteinHypernetwork.networkStates.NetworkState;
import org.apache.commons.collections15.Transformer;

/**
 * Transforms a network state into a protein subnetwork
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkStateToSubnetwork implements Transformer<NetworkState, ProteinSubnetwork> {

  private NetworkEntitiesToSubnetwork networkEntitiesToSubgraph;

  public NetworkStateToSubnetwork() {
    networkEntitiesToSubgraph = new NetworkEntitiesToSubnetwork();
  }
  
  public NetworkStateToSubnetwork(ProteinSubnetwork ppin, boolean noSelfInteractions,  boolean addNeighbors) {
    networkEntitiesToSubgraph = new NetworkEntitiesToSubnetwork(ppin, noSelfInteractions, addNeighbors);
  }
  
  

  /**
   * Returns protein subnetwork for a given network state.
   *
   * @param state the network state
   * @return the protein subnetwork
   */
  public ProteinSubnetwork transform(NetworkState state) {
    return networkEntitiesToSubgraph.transform(state);
  }

}
