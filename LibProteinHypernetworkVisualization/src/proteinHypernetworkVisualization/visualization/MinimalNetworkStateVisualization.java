/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinHypernetworkVisualization.visualization;

import java.util.Collection;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import proteinHypernetwork.ProteinHypernetwork;

/**
 *
 * @author koester
 */
public interface MinimalNetworkStateVisualization extends Visualization {
    public void setMinimalNetworkStates(ProteinHypernetwork hypernetwork, Collection<MinimalNetworkState> minimalNetworkStates);
}
