/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package util.lcma;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A Clique.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Clique<V, E> extends Subgraph<V> {

  private UndirectedGraph<V, E> graph;

  /**
   * Constructor of class clique.
   *
   * @param n a local neighbourhood graph
   */
  public Clique(LocalNeighbourhoodGraph<V, E> n) {
    vertices = new HashSet<V>();
    for (V v : n) {
      vertices.add(v);
    }
    graph = n.getGraph();
  }

  /**
   * Return the clique subgraph.
   *
   * @return the subgraph
   */
  public UndirectedGraph<V, E> getGraph() {
    return graph;
  }

  public Iterator<V> iterator() {
    return vertices.iterator();
  }

  /**
   * Return the number of edges.
   *
   * @return the number of edges
   */
  public int getEdgeCount() {
    return getVertexCount() * (getVertexCount() - 1) / 2;
  }

  /**
   * Returns the degree in this clique. Every node has the same degree inside the
   * subgraph.
   *
   * @return the degree
   */
  public int getDegree() {
    return vertices.size() - 1;
  }

  @Override
  public double density() {
    return 1;
  }

  @Override
  public int getVertexCount() {
    return vertices.size();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Clique other = (Clique) obj;
    if (this.vertices != other.vertices && (this.vertices == null || !this.vertices.equals(other.vertices))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return vertices.hashCode();
  }

  @Override
  public double neighbourAffinity(Subgraph<V> s) {

    int commonVertices = 0;
    for(V v : s) {
      if(vertices.contains(v))
        commonVertices++;
    }

    return ((double)commonVertices*commonVertices) / (getVertexCount() * s.getVertexCount());
  }
}
