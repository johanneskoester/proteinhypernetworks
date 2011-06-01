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
public class TwoHopIterator2<V> implements Iterator<RealSupervertex<V>> {

  private UndirectedGraph<V,?> graph;
  private Map<V, RealSupervertex<V>> supervertices;
  private Deque<RealSupervertex<V>> queue = new ArrayDeque<RealSupervertex<V>>();
  private Map<RealSupervertex<V>, Integer> dist = new HashMap<RealSupervertex<V>, Integer>();

  public TwoHopIterator2(RealSupervertex<V> u, UndirectedGraph<V, ?> graph, Map<V, RealSupervertex<V>> supervertices) {
    this.graph = graph;
    this.supervertices = supervertices;

    queue.add(u);
    dist.put(u, 0);

    next();
  }

  public boolean hasNext() {
    return !queue.isEmpty();
  }

  public RealSupervertex<V> next() {
    RealSupervertex<V> u = queue.removeFirst();
    int du = dist.get(u);

    if(du < 2) {
      NeighborIterator<V> neighborIterator = new NeighborIterator<V>(u, graph);
      while(neighborIterator.hasNext()) {
        RealSupervertex<V> v = supervertices.get(neighborIterator.next());
        if(!dist.containsKey(v)) {
          queue.addLast(v);
          dist.put(v, du+1);
        }

      }
    }
    return u;
  }

  public void remove() {
    throw new UnsupportedOperationException("Not supported.");
  }
}
