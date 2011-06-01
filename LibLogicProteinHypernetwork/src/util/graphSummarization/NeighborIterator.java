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
public class NeighborIterator<V> extends BFSIterator<V> {

  public NeighborIterator(Iterable<V> startnodes, UndirectedGraph<V, ?> graph) {
    super(startnodes, graph);
  }

  public NeighborIterator(V startnode, UndirectedGraph<V, ?> graph) {
    super(startnode, graph);
  }

  @Override
  protected boolean dive(int dist) {
    return dist < 1;
  }
}
