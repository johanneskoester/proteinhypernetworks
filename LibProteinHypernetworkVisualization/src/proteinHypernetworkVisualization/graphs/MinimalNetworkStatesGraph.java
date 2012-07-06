/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinHypernetworkVisualization.graphs;

import java.util.Collection;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;

/**
 *
 * @author koester
 */
public interface MinimalNetworkStatesGraph {
    public void setMinimalNetworkStates(Collection<MinimalNetworkState> states);
}
