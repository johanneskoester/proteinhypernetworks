/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcode;

import logicProteinHypernetwork.analysis.complexes.NetworkStateToSubnetwork;
import logicProteinHypernetwork.analysis.complexes.PPINMinusMissingInSubnetwork;
import logicProteinHypernetwork.analysis.complexes.ProteinSubnetwork;
import logicProteinHypernetwork.networkStates.NetworkState;

/**
 *
 * @author koester
 */
public class MCODENetworkStateToSubnetwork extends NetworkStateToSubnetwork {
  
  private PPINMinusMissingInSubnetwork ppinMinusMissingInSubnetwork;
  
  public MCODENetworkStateToSubnetwork(ProteinSubnetwork ppin) {
    super(ppin, false, false);
    ppinMinusMissingInSubnetwork = new PPINMinusMissingInSubnetwork(ppin);
  }
  
  /**
   * Returns protein subnetwork for a given network state.
   *
   * @param state the network state
   * @return the protein subnetwork
   */
  @Override
  public ProteinSubnetwork transform(NetworkState state) {
    ProteinSubnetwork s = networkEntitiesToSubgraph.transform(state);
    return ppinMinusMissingInSubnetwork.transform(s);
  }
}
