/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.graphSummarization;

import java.util.HashMap;
import java.util.Map;
import util.fibonacciHeap.FibonacciHeap;
import util.fibonacciHeap.FibonacciHeapNode;

/**
 *
 * @author koester
 */
public class MergeQueue2<V> {
  private Map<PseudoSupervertex<V>, FibonacciHeapNode<PseudoSupervertex<V>>> nodeMap =
          new HashMap<PseudoSupervertex<V>, FibonacciHeapNode<PseudoSupervertex<V>>>();
  private FibonacciHeap<PseudoSupervertex<V>> heap =
          new FibonacciHeap<PseudoSupervertex<V>>();

  public boolean isEmpty() {
    return heap.isEmpty();
  }

  public boolean contains(PseudoSupervertex<V> candidate) {
    return nodeMap.containsKey(candidate);
  }

  public PseudoSupervertex<V> remove() {
    /*PseudoSupervertex<V> r =  heap.remove();
    score.remove(r);*/
    PseudoSupervertex<V> r = heap.removeMin().getData();
    nodeMap.remove(r);
    return r;
  }

  public void remove(PseudoSupervertex<V> pair) {
    FibonacciHeapNode<PseudoSupervertex<V>> node = nodeMap.remove(pair);
    if(node != null)
      heap.delete(node);
    /*heap.remove(pair);
    score.remove(pair);*/
  }

  public void add(PseudoSupervertex<V> candidate, float score) {
    if(score > 0 && !nodeMap.containsKey(candidate)) {
      score = 1 - score;
      FibonacciHeapNode<PseudoSupervertex<V>> node = new FibonacciHeapNode<PseudoSupervertex<V>>(candidate);
      nodeMap.put(candidate, node);
      heap.insert(node, score);
    }
    

    /*if (!this.score.containsKey(candidate)) {
      if (score > 0) {
        this.score.put(candidate, score);
        this.heap.add(candidate);
      }
    }*/
  }

  public int size() {
    return heap.size();
  }

  
}
