/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

import java.util.Collection;

/**
 * A class to filter a collection.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class Filter<T> extends ParameterizedFilter<T, Object> {

  public Collection<T> filter(Iterable<T> toFilter) {
    return filter(toFilter, new Object());
  }

  @Override
  public boolean compare(T o, Object q) {
    return compare(o);
  }

  public abstract boolean compare(T o);

}
