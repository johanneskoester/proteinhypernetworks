/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

/**
 *
 * @author koester
 */
public interface Supervertex<V> extends Iterable<V> {

  public int size();

  public boolean contains(V v);
  
}
