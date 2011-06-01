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
public class TwoHopIterator<V> extends BFSIterator<V> {

  public TwoHopIterator(Iterable<V> startnodes, UndirectedGraph<V, ?> graph) {
    super(startnodes, graph);
  }

  @Override
  protected boolean dive(int dist) {
    return dist < 2;
  }
}
