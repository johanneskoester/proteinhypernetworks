/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author koester
 */
public class SupervertexCost<V, E> {

  public int supervertexCost(Supervertex<V> u, UndirectedGraph<V, E> graph, Map<V, ? extends Supervertex<V>> supervertices) {
    int cost = superedgesCost(u, graph, supervertices);
    cost += loopCost(u, graph, supervertices);

    return cost;
  }

  protected int superedgesCost(Supervertex<V> u, UndirectedGraph<V, E> graph, Map<V, ? extends Supervertex<V>> supervertices) {
    Map<Supervertex<V>, Integer> edges = new HashMap<Supervertex<V>, Integer>();

    for (V v : u) {
      for (V w : graph.getNeighbors(v)) {
        Supervertex<V> x = supervertices.get(w);
        if (!u.contains(w)) {
          Integer e = edges.get(x);
          if (e == null) {
            e = 0;
          }
          edges.put(x, e + 1);
        }
      }
    }

    int edgesCost = 0;

    for (Supervertex<V> v : edges.keySet()) {
      int maxEdges = maxEdges(u, v);
      int realEdges = edges.get(v);
      /** counts edges between supernodes! */
      edgesCost += Math.min(maxEdges - realEdges + 1, realEdges);
    }

    return edgesCost;
  }

  protected int loopCost(Supervertex<V> u, UndirectedGraph<V, E> graph, Map<V, ? extends Supervertex<V>> supervertices) {
    int maxEdges = u.size() * (u.size() - 1) / 2; // this would be a clique
    int realEdges = realEdges(u, graph, supervertices);

    return Math.min(maxEdges - realEdges + 1, realEdges);
  }

  private int maxEdges(Supervertex<V> u, Supervertex<V> v) {
    return u.size() * v.size();
  }

  private int realEdges(Supervertex<V> u, UndirectedGraph<V, E> graph, Map<V, ? extends Supervertex<V>> supervertices) {
    int edges = u.size() * (u.size() - 1) / 2;

    List<V> vertices = new ArrayList<V>();
    for (V v : u) {
      vertices.add(v);
    }

    for (int i = 0; i < vertices.size(); i++) {
      V v = vertices.get(i);
      for (int j = i + 1; j < vertices.size(); j++) {
        V w = vertices.get(j);
        if (!graph.isNeighbor(v, w)) {
          edges--;
        }
      }
    }

    return edges;
  }
}
