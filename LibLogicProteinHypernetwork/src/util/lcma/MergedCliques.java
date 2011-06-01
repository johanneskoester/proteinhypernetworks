/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package util.lcma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A subgraph that consists of merged cliques.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MergedCliques<V, E> extends Subgraph<V> {
  private int edgeCount = 0;

  /**
   * Constructor of class MergedCliques.
   *
   * @param subgraph an initial subgraph (either a clique or already merged cliques)
   */
  public MergedCliques(Subgraph<V> subgraph) {
    edgeCount = subgraph.getEdgeCount();
    vertices = new HashSet<V>(subgraph.vertices);
  }


  /**
   * Merge with a given subgraph.
   *
   * @param s the subgraph
   */
  public void merge(Subgraph<V> s) {
    if(s instanceof Clique) {
      merge((Clique)s);
    }
    else {
      merge((MergedCliques)s);
    }
  }

  /**
   * Merge with a given clique.
   *
   * @param clique the clique
   */
  public void merge(Clique<V, E> clique) {
    int intersection = 0;
    for (V v : clique) {
      if (vertices.contains(v)) {
        intersection++;
      } else {
        vertices.add(v);
      }
    }

    edgeCount += clique.getEdgeCount() - (((intersection - 1) * intersection) / 2);
  }

  /**
   * Merge with a given merged cliques.
   *
   * @param m merged cliques
   */
  public void merge(MergedCliques<V,E> m) {
    /** @todo remove collection of cliques since they are not needed with this new approximation */
    int intersection = 0;
    for (V v : m) {
      if (vertices.contains(v)) {
        intersection++;
      } else {
        vertices.add(v);
      }
    }

    edgeCount += m.getEdgeCount() - (((intersection - 1) * intersection) / 2);
  }

  /**
   * Iterate over vertices.
   *
   * @return an iterator
   */
  public Iterator<V> iterator() {
    return vertices.iterator();
  }

  /**
   * Returns the neighbour affinity to a given subgraph.
   *
   * @param s the subgraph
   * @return the neighbour affinity
   */
  public double neighbourAffinity(Subgraph<V> s) {
    int commonVertices = 0;
    for(V v : s) {
      if(vertices.contains(v))
        commonVertices++;
    }

    return ((double)commonVertices*commonVertices) / (getVertexCount() * s.getVertexCount());
  }

  @Override
  public int getVertexCount() {
    return vertices.size();
  }

  @Override
  public int getEdgeCount() {
    return edgeCount;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MergedCliques<V, E> other = (MergedCliques<V, E>) obj;
    if (this.vertices != other.vertices && (this.vertices == null || !this.vertices.equals(other.vertices))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return vertices.hashCode();
  }
}
