/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.graphs;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
import org.apache.commons.collections15.CollectionUtils;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetworkVisualization.graphs.ProteinComplexesGraph;

/**
 * A graph of protein complexes. Edges are drawn on significant overlap.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class JungProteinComplexesGraph extends UndirectedSparseGraph<Complex, Object> implements ProteinComplexesGraph {

  public void setProteinComplexes(ProteinHypernetwork hypernetwork, Collection<Complex> complexes) {
    int index = 0;
    for(Complex c : complexes) {
      addVertex(c);
    }

    // draw edges between overlapping complexes
    Complex[] cs = new Complex[complexes.size()];
    complexes.toArray(cs);
    for(int i=0; i< cs.length; i++) {
      for(int j = i+1; j < cs.length; j++) {
        int isize = CollectionUtils.intersection(cs[i], cs[j]).size();
        if(((float)isize) / (cs[i].size() + cs[j].size() - isize) > 0.2)
          addEdge(new Object(), cs[i], cs[j]);
      }
    }
  }
}
