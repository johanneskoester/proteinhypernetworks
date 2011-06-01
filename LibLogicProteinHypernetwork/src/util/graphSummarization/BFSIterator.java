/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author koester
 */
public class BFSIterator<V> implements Iterator<V> {

  private UndirectedGraph<V, ?> graph;
  private Deque<V> queue = new ArrayDeque<V>();
  protected Map<V, Integer> dist = new HashMap<V, Integer>();
  private boolean loop = false;

  public BFSIterator(Iterable<V> startnodes, UndirectedGraph<V, ?> graph) {
    this.graph = graph;
    for (V u : startnodes) {
      dist.put(u, 0);
      queue.addLast(u);
    }
    for(V u : startnodes) {
      next();
    }
  }

  public BFSIterator(V u, UndirectedGraph<V, ?> graph) {
    this.graph = graph;
    dist.put(u, 0);
    queue.addLast(u);
    next();
  }

  public boolean hasNext() {
    return !queue.isEmpty();
  }

  public V next() {
    V u = queue.removeFirst();
    int du = dist.get(u);
    if (dive(du)) {
      for (V v : graph.getNeighbors(u)) {
        if (visit(v, du)) {
          dist.put(v, du + 1);
          queue.addLast(v);
        }
      }
    }
    return u;
  }

  public void remove() {
    throw new UnsupportedOperationException("Not supported.");
  }

  protected boolean dive(int dist) {
    return true;
  }

  protected boolean visit(V v, int du) {
    return !dist.containsKey(v);
  }
}
