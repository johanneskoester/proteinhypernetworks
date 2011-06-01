/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes.gs;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import util.graphSummarization.Supervertex;
import util.graphSummarization.SupervertexCost;

/**
 * Class ConstrainedSupervertexCost provdides a cost function for supervertices
 * that take constraints into account.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstrainedSupervertexCost extends SupervertexCost<Protein, Interaction> {

  private MinimalNetworkStates minimalNetworkStates;

  /**
   * Constructor of class ConstrainedSupervertexCost.
   *
   * @param minimalNetworkStates all minimal network states
   */
  public ConstrainedSupervertexCost(MinimalNetworkStates minimalNetworkStates) {
    this.minimalNetworkStates = minimalNetworkStates;
  }

  @Override
  public int supervertexCost(Supervertex<Protein> u, UndirectedGraph<Protein, Interaction> graph, Map<Protein, ? extends Supervertex<Protein>> supervertices) {
    int cost = super.supervertexCost(u, graph, supervertices);

    List<Protein> vertices = new ArrayList<Protein>();
    for (Protein p : u) {
      vertices.add(p);
    }

    List<MinimalNetworkState> mns = new ArrayList<MinimalNetworkState>();
    for (int i = 0; i < vertices.size(); i++) {
      Protein p = vertices.get(i);
      for (int j = i + 1; j < vertices.size(); j++) {
        Protein q = vertices.get(j);
        Interaction e = graph.findEdge(p, q);
        if (minimalNetworkStates.contains(e)) {
          mns.addAll(minimalNetworkStates.getMinimalNetworkStates(e));
        }
      }
    }

    for (int i = 0; i < mns.size(); i++) {
      MinimalNetworkState m = mns.get(i);
      for (int j = i + 1; j < mns.size(); j++) {
        MinimalNetworkState n = mns.get(j);
        if(m.isClash(n)) {
          //cost *= 2;
          //cost += 1;
          return Integer.MAX_VALUE;
        }
      }
    }

    return cost;
  }
}
