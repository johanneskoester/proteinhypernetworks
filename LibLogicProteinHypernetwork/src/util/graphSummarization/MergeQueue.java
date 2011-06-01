/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.graphSummarization;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections15.buffer.PriorityBuffer;

/**
 *
 * @author koester
 */
public class MergeQueue<V> {

  private Map<PseudoSupervertex<V>, Float> score = new HashMap<PseudoSupervertex<V>, Float>();
  private PriorityBuffer<PseudoSupervertex<V>> buffer =
          new PriorityBuffer<PseudoSupervertex<V>>(false, new Comparator<PseudoSupervertex<V>>() {

    public int compare(PseudoSupervertex<V> o1, PseudoSupervertex<V> o2) {
      float c = score.get(o1) - score.get(o2);
      if (c > 0) {
        return 1;
      }
      if (c < 0) {
        return -1;
      }
      return 0;
    }
  });

  public boolean isEmpty() {
    return buffer.isEmpty();
  }

  public PseudoSupervertex<V> remove() {
    PseudoSupervertex<V> r =  buffer.remove();
    score.remove(r);
    return r;
  }

  public void remove(PseudoSupervertex<V> pair) {
    buffer.remove(pair);
    score.remove(pair);
  }

  public void update(PseudoSupervertex<V> pair, float score) {
    remove(pair);
    add(pair, score);
  }

  public void add(PseudoSupervertex<V> pair, float score) {
    if (!this.score.containsKey(pair)) {
      if (score > 0) {
        this.score.put(pair, score);
        this.buffer.add(pair);
      }
    }
  }

  public int size() {
    return buffer.size();
  }

  
}
