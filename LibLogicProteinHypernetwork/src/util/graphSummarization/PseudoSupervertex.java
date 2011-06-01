/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

import edu.uci.ics.jung.graph.util.Pair;
import java.util.Iterator;
import org.apache.commons.collections15.SetUtils;
import org.apache.commons.collections15.iterators.IteratorChain;

/**
 *
 * @author koester
 */
public class PseudoSupervertex<V> implements Supervertex<V> {
  private Pair<RealSupervertex<V>> uv;
  private int hashCode;
  
  public PseudoSupervertex(RealSupervertex<V> u, RealSupervertex<V> v) {
    uv = new Pair<RealSupervertex<V>>(u, v);
    hashCode = SetUtils.hashCodeForSet(uv);
  }

  public RealSupervertex<V> getSecond() {
    return uv.getSecond();
  }

  public RealSupervertex<V> getFirst() {
    return uv.getFirst();
  }

  public int size() {
    return uv.getFirst().size() + uv.getSecond().size();
  }

  public boolean contains(V v) {
    return uv.getFirst().contains(v) || uv.getSecond().contains(v);
  }

  public Iterator<V> iterator() {
    return new IteratorChain<V>(uv.getFirst().iterator(), uv.getSecond().iterator());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    final PseudoSupervertex<V> other = (PseudoSupervertex<V>) obj;
    if (this.hashCode != other.hashCode) {
      return false;
    }
    
    if (!SetUtils.isEqualSet(uv, other.uv)) {
      return false;
    }
    
    return true;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    String s = "";
    for(Supervertex u : uv)
      s += " " + u;
    return s;
  }
}
