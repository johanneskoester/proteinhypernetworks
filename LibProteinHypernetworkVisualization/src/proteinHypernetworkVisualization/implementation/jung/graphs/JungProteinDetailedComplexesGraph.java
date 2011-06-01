/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.graphs;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import logicProteinHypernetwork.analysis.complexes.Complex;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetworkVisualization.graphs.ProteinComplexesGraph;
import proteinHypernetworkVisualization.implementation.jung.graphs.JungProteinDetailedComplexesGraph.ProteinVertex;

/**
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class JungProteinDetailedComplexesGraph extends UndirectedSparseGraph<ProteinVertex, Object> implements ProteinComplexesGraph {

  public void setProteinComplexes(ProteinHypernetwork hypernetwork, Collection<Complex> complexes) {
    for(Complex c : complexes) {
      //addVertex(c);

      Map<Protein, ProteinVertex> pvmap = new HashMap<Protein, ProteinVertex>();

      for(Interaction i : c.getSubnetwork().getEdges()) {
        Collection<ProteinVertex> pvs = new ArrayList<ProteinVertex>(2);
        for(Protein p : i.getProteins()) {
          ProteinVertex pv = pvmap.get(p);
          if(pv == null) {
            pv = new ProteinVertex(p);
            pvmap.put(p, pv);
          }
          addVertex(pv);
          pvs.add(pv);
        }

        addEdge(new Object(), pvs);
      }

    }

    // draw edges between overlapping complexes
    /*Complex[] cs = new Complex[complexes.size()];
    complexes.toArray(cs);
    for(int i=0; i< cs.length; i++) {
      for(int j = i+1; j < cs.length; j++) {
        if(CollectionUtils.containsAny(cs[i], cs[j]))
          addEdge(new Object(), cs[i], cs[j]);
      }
    }*/
  }

  public class ProteinVertex {
    private Protein protein;

    public ProteinVertex(Protein protein) {
      this.protein = protein;
    }

    public String toString() {
      return protein.toString();
    }
  }

}
