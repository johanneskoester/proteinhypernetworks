/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.graphSummarization;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import util.ProgressBean;

/**
 *
 * @author koester
 */
public class GraphSummarization<V, E> {

  private ProgressBean progressBean = new ProgressBean();
  private SupervertexCost<V, E> supervertexCost;

  public GraphSummarization() {
    this(new SupervertexCost<V, E>());
  }

  public GraphSummarization(SupervertexCost<V, E> supervertexCost) {
    this.supervertexCost = supervertexCost;
  }

  public ProgressBean getProgressBean() {
    return progressBean;
  }

  public Collection<? extends Iterable<V>> graphSummarization(UndirectedGraph<V, E> graph) {
    Map<V, RealSupervertex<V>> supervertices = new HashMap<V, RealSupervertex<V>>();
    for (V u : graph.getVertices()) {
      supervertices.put(u, new RealSupervertex<V>(u));
    }

    //MergeQueue<V> queue = new MergeQueue<V>();
    MergeQueue2<V> queue = new MergeQueue2<V>();

    for (RealSupervertex<V> u : supervertices.values()) {
      Iterator<V> twoHopIterator = new TwoHopIterator<V>(u, graph);
      while (twoHopIterator.hasNext()) {
        RealSupervertex<V> v = supervertices.get(twoHopIterator.next());
        if (v != u) {
          PseudoSupervertex<V> candidate = new PseudoSupervertex<V>(u, v);
          if (!queue.contains(candidate)) {
            float score = costReduction(candidate, graph, supervertices);
            queue.add(candidate, score);
          }
        }
      }
    }
    int max = queue.size();

    while (!queue.isEmpty()) {
      System.out.println(queue.size());
      // create the supervertex
      PseudoSupervertex<V> uv = queue.remove();
      RealSupervertex<V> w = new RealSupervertex<V>(uv);
      for (V v : w) {
        supervertices.put(v, w);
      }

      {
        Iterator<RealSupervertex<V>> twoHopIterator = new TwoHopIterator2<V>(w, graph, supervertices);
        while (twoHopIterator.hasNext()) {
          RealSupervertex<V> x = twoHopIterator.next();
          PseudoSupervertex<V> ux = new PseudoSupervertex<V>(uv.getFirst(), x);
          PseudoSupervertex<V> vx = new PseudoSupervertex<V>(uv.getSecond(), x);
          queue.remove(ux);
          queue.remove(vx);

          PseudoSupervertex<V> wx = new PseudoSupervertex<V>(w, x);

          float score = costReduction(wx, graph, supervertices);
          queue.add(wx, score);
        }
      }

      {
        Iterator<V> neighborIterator = new NeighborIterator<V>(w, graph);
        while (neighborIterator.hasNext()) {
          RealSupervertex<V> x = supervertices.get(neighborIterator.next());

          if (x != w) {
            Set<RealSupervertex<V>> visited = new HashSet<RealSupervertex<V>>();
            Iterator<V> twoHopIterator = new TwoHopIterator<V>(x, graph);
            while (twoHopIterator.hasNext()) {
              RealSupervertex<V> y = supervertices.get(twoHopIterator.next());
              if (y != x && y != w) {
                if (!visited.contains(y)) {
                  /** @todo remove the need of contains by more intelligent neighborIterator */
                  visited.add(y);
                  PseudoSupervertex<V> xy = new PseudoSupervertex<V>(x, y);
                  queue.remove(xy);

                  float score = costReduction(xy, graph, supervertices);
                  queue.add(xy, score);
                }
              }
            }
          }
        }
      }

      progressBean.setProgress(max - queue.size(), max);
    }

    return new HashSet<Supervertex<V>>(supervertices.values());
  }

  protected float costReduction(PseudoSupervertex<V> uv, UndirectedGraph<V, E> graph, Map<V, ? extends Supervertex<V>> supervertices) {
    int costSoFar = supervertexCost.supervertexCost(uv.getFirst(), graph, supervertices);
    costSoFar += supervertexCost.supervertexCost(uv.getSecond(), graph, supervertices);
    float mergeCost = supervertexCost.supervertexCost(uv, graph, supervertices);

    float reduction = (costSoFar - mergeCost) / costSoFar;

    return reduction;
  }
}
