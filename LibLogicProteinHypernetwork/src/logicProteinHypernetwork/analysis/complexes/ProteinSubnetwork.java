/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * A protein subnetwork, represented as an undirected graph.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinSubnetwork extends UndirectedSparseGraph<Protein, Interaction> {

  private Map<Protein, Integer> degree = new HashMap<Protein, Integer>();

  /**
   * Creates a new empty protein subnetwork.
   */
  public ProteinSubnetwork() {
  }

  /**
   * Creates a protein subnetwork out of a given undirected graph.
   * 
   * @param g the graph
   */
  public ProteinSubnetwork(UndirectedGraph<Protein, Interaction> g) {
    for(Protein p : g.getVertices()) {
      addVertex(p);
    }
    for(Interaction i : g.getEdges()) {
      addEdge(i, g.getEndpoints(i));
    }
  }

  /**
   * Set an artificial degree for a given protein.
   *
   * @param p the protein
   * @param degree the degree
   */
  public void setDegree(Protein p, int degree) {
    this.degree.put(p, degree);
  }

  @Override
  public int degree(Protein p) {
    Integer deg = degree.get(p);
    if(deg == null)
      return super.degree(p);
    return deg;
  }

  /**
   * Returns the density of the subnetwork.
   *
   * @return the density
   */
  public float getDensity() {
    float d = 2 * getEdgeCount();
    return d / getVertexCount() * (getVertexCount() - 1);
  }

  /**
   * Comparator that looks at density.
   */
  public static class DensityComparator implements Comparator<ProteinSubnetwork> {

    public int compare(ProteinSubnetwork s, ProteinSubnetwork s1) {
      float diff = s.getDensity() - s1.getDensity();
      if(diff < 0)
        return -1;
      if(diff > 0)
        return 1;
      return 0;
    }

  }
}
