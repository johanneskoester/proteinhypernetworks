/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A filter that can be parameterized.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class ParameterizedFilter<T,E> {
  

  public ParameterizedFilter() {
  }

  public boolean contains(Iterable<T> toFilter, E o) {
    for(T q : toFilter) {
      if(compare(q,o))
        return true;
    }
    return false;
  }

  public Collection<T> filter(Iterable<T> toFilter, E o) {
    Collection<T> filtered = new ArrayList<T>();
    for(T q : toFilter) {
      if(compare(q,o)) {
        filtered.add(q);
      }
    }
    return filtered;
  }

  public abstract boolean compare(T o, E q);
}
