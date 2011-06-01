/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.graphSummarization;

import edu.uci.ics.jung.graph.UndirectedGraph;

/**
 *
 * @author koester
 */
public class LoopIterator<V> extends BFSIterator<V> {

  private boolean loop = false;

  public LoopIterator(Iterable<V> startnodes, UndirectedGraph<V, ?> graph) {
    super(startnodes, graph);
  }

  @Override
  public boolean hasNext() {
    return super.hasNext() && !loop;
  }

  @Override
  protected boolean dive(int dist) {
    return dist < 1;
  }

  @Override
  protected boolean visit(V v, int du) {
    boolean ret = !loop && du == 0 && dist.containsKey(v);
    if (ret) {
      loop = true;
    }
    return ret;
  }
}
