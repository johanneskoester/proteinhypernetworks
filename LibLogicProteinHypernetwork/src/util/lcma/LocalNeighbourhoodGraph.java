/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package util.lcma;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections15.buffer.PriorityBuffer;

/**
 * A local neighbourhood graph. Consists of a vertex and all its neighbours.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class LocalNeighbourhoodGraph<V, E> extends Subgraph<V> {

  private UndirectedGraph<V, E> graph;
  private int edgeCount = 0;
  private Map<V, Integer> degrees = new HashMap<V, Integer>();
  private V vertex;

  /**
   * Constructor of class LocalNeighbourhoodGraph.
   * @param vertex the vertex
   * @param graph the graph
   */
  public LocalNeighbourhoodGraph(V vertex, UndirectedGraph<V, E> graph) {
    vertices = new MinDegreeHeap();
    this.vertex = vertex;
    this.graph = graph;
    degrees.put(vertex, 0);
    for (V v : graph.getNeighbors(vertex)) {
      degrees.put(v, 0);
    }

    for (V v : degrees.keySet()) {
      int degree = 0;
      for (V n : graph.getNeighbors(v)) {
        if (degrees.containsKey(n)) {
          degree++;
        }
      }
      degrees.put(v, degree);
      vertices.add(v);
    }

    for(Integer d : degrees.values())
        edgeCount += d;
    edgeCount /= 2;
  }

  /**
   * Return the underlying graph.
   *
   * @return the graph
   */
  public UndirectedGraph<V, E> getGraph() {
    return graph;
  }

  /**
   * Returns the vertex.
   *
   * @return the vertex
   */
  public V getVertex() {
    return vertex;
  }

  @Override
  public int getVertexCount() {
    return vertices.size();
  }

  @Override
  public int getEdgeCount() {
    return edgeCount;
  }

  public Iterator<V> iterator() {
    return vertices.iterator();
  }

  /**
   * Remove a vertex.
   *
   * @param v the vertex to remove
   */
  public void removeVertex(V v) {
    edgeCount -= degrees.get(v);
    degrees.remove(v);
    vertices.remove(v);
  }

  /**
   * Preview the density without a given vertex.
   *
   * @param v the vertex
   * @return the density
   */
  public double densityWithoutVertex(V v) {
    return density(getVertexCount()-1, getEdgeCount() - degrees.get(v));
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

  /**
   * A heap to store vertices in ascending degree.
   */
  private class MinDegreeHeap extends PriorityBuffer<V> {

    /**
     * Constructor of class MinDegreeHeap.
     */
    public MinDegreeHeap() {
      super(true);

      comparator = new Comparator<V>() {

        public int compare(V o1, V o2) {
          int comp = degrees.get(o1) - degrees.get(o2);
          if(comp != 0) {
            return comp;
          }
          else {
            return o1.toString().compareTo(o2.toString());
          }
        }
      };
    }

    @Override
    public boolean remove(Object o) {
      V v = (V)o;
      super.remove(v);
      for (int i = 0; i < elements.length; i++) {
        if (elements[i] != null && graph.isNeighbor(elements[i], v)) {
          degrees.put(elements[i], degrees.get(elements[i]) - 1);
          percolateUpMinHeap(i);
        }
      }

      return true;
    }
  }
}
