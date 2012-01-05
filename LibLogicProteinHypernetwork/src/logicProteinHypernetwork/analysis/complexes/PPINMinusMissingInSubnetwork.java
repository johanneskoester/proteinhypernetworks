/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes;

import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 *
 * @author Johannes Koester <johannes.koester@tu-dortmund.de>
 */
public class PPINMinusMissingInSubnetwork implements Transformer<ProteinSubnetwork, ProteinSubnetwork> {
  private ProteinSubnetwork ppin;

  public PPINMinusMissingInSubnetwork(ProteinSubnetwork ppin) {
    this.ppin = ppin;
  }

  public ProteinSubnetwork transform(ProteinSubnetwork s) {
    ProteinSubnetwork graph = new ProteinSubnetwork(ppin);
    
    for(Interaction i : ppin.getEdges()) {
      Pair<Protein> ep = ppin.getEndpoints(i);
      if(s.containsVertex(ep.getFirst()) && s.containsVertex(ep.getSecond())
              && !s.containsEdge(i)) {
        graph.removeEdge(i);
      }
    }
    
    return graph;
  }
  
  
}
