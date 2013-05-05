/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections15.SetUtils;

/**
 *
 * @author koester
 */
public class RealSupervertex<V> implements Supervertex<V> {
  private Collection<V> vertices;
  private int hashCode;

  public RealSupervertex(V v) {
    vertices = new ArrayList<V>();
    vertices.add(v);
    hashCode = v.hashCode();
  }

  public RealSupervertex(PseudoSupervertex<V> uv) {
    vertices = new ArrayList<V>(uv.getFirst().size() + uv.getSecond().size());
    vertices.addAll(uv.getFirst().vertices);
    vertices.addAll(uv.getSecond().vertices);
    hashCode = SetUtils.hashCodeForSet(vertices);
  }

  public int size() {
    return vertices.size();
  }

  public boolean contains(V v) {
    return vertices.contains(v);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RealSupervertex<V> other = (RealSupervertex<V>) obj;
    if (this.hashCode != other.hashCode) {
      return false;
    }
    if (this.vertices != other.vertices && (this.vertices == null || !this.vertices.equals(other.vertices))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  public Iterator<V> iterator() {
    return vertices.iterator();
  }

  @Override
  public String toString() {
    String s = "";
    for(V v : vertices)
      s += " " + v;
    return s;
  }
}
