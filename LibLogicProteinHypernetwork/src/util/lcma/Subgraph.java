/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package util.lcma;

import java.util.Collection;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class Subgraph<V> implements Iterable<V> {

  protected Collection<V> vertices;

  public Collection<V> getVertices() {
    return vertices;
  }

  /**
   * Returns the number of vertices.
   *
   * @return the number of vertices
   */
  public abstract int getVertexCount();

  /**
   * Returns the number of edges.
   *
   * @return the number of edges
   */
  public abstract int getEdgeCount();

  /**
   * Returns the neighbour affinity to a given subgraph.
   *
   * @param s the subgraph
   * @return the neighbour affinity
   */
  public abstract double neighbourAffinity(Subgraph<V> s);

  /**
   * Returns true if a significant overlap between this and a given subgraph exists.
   *
   * @param s a subgraph
   * @param similarityThreshold the similarity threshold
   * @return true if a significant overlap exists
   */
  public boolean significantOverlap(Subgraph<V> s, double similarityThreshold) {
    Collection<V> v1;
    Collection<V> v2;
    if(vertices.size() > s.vertices.size()) {
      v1 = s.vertices;
      v2 = vertices;
    }
    else {
      v1 = vertices;
      v2 = s.vertices;
    }

    int common = 0;
    int vv = v1.size() * v2.size();
    for(V v : v1) {
      if(v2.contains(v)) {
        common++;
        if(neighbourAffinity(common, vv) > similarityThreshold)
          return true;
      }
    }
    return false;
  }

  /**
   * Returns the neighbour affinity for a given intersection and product of vertices.
   *
   * @param commonVertices the intersection
   * @param vertexProduct the product
   * @return the neighbour affinity
   */
  private float neighbourAffinity(int commonVertices, int vertexProduct) {
    return ((float)commonVertices*commonVertices) / vertexProduct;
  }

  /**
   * Returns the density for a given number of vertices and edges.
   *
   * @param vertexCount the vertex number
   * @param edgeCount the edge number
   * @return the density
   */
  protected static double density(int vertexCount, int edgeCount) {
    return (2.0 * edgeCount) / (vertexCount * (vertexCount - 1));
  }

  /**
   * Returns the density for this subgraph.
   *
   * @return the density
   */
  public double density() {
    return density(getVertexCount(), getEdgeCount());
  }
}
