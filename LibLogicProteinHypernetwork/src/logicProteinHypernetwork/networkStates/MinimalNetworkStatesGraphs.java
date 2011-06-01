/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import proteinHypernetwork.NetworkEntity;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MinimalNetworkStatesGraphs {

  private DirectedGraph<NetworkEntity, Object> activationGraph =
          new DirectedSparseGraph<NetworkEntity, Object>();
  private DirectedGraph<NetworkEntity, Object> inhibitionGraph =
          new DirectedSparseGraph<NetworkEntity, Object>();

  /**
   * Constructor of class minimal network states graph.
   *
   * @param mns the minimal network states
   */
  public MinimalNetworkStatesGraphs(MinimalNetworkStates mns) {
    buildGraph(mns);
  }

  /**
   * Returns the actiavtion part of the graph.
   *
   * @return the activation graph
   */
  public DirectedGraph<NetworkEntity, Object> getActivationGraph() {
    return activationGraph;
  }

  /**
   * Returns the inhibition part of the graph.
   *
   * @return the inhibition graph
   */
  public DirectedGraph<NetworkEntity, Object> getInhibitionGraph() {
    return inhibitionGraph;
  }

  /**
   * Builds the combined (activation and inhibition) minimal network state graph.
   *
   * @param mns the minimal network states
   */
  private void buildGraph(MinimalNetworkStates mns) {
    for (NetworkEntity e : mns.getPossibleEntities()) {
      activationGraph.addVertex(e);
      inhibitionGraph.addVertex(e);
    }
    for (NetworkEntity e : mns.getPossibleEntities()) {
      for (MinimalNetworkState s : mns.getMinimalNetworkStates(e)) {
        for (NetworkEntity f : s.getNecessary()) {
          activationGraph.addEdge(new Object(), f, e);
        }
        for (NetworkEntity f : s.getImpossible()) {
          inhibitionGraph.addEdge(new Object(), e, f);
        }
      }

    }
  }
}
