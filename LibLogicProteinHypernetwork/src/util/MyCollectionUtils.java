/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

import java.util.Collection;
import org.apache.commons.collections15.CollectionUtils;

/**
 * Custom extensions to CollectionUtils.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MyCollectionUtils extends CollectionUtils {

  /**
   * Calculate the size of intersection between two collections.
   *
   * @param c1 a collection
   * @param c2 a collection
   * @return the size of intersection
   */
  public static int intersectionSize(Collection c1, Collection c2) {
    int size = 0;
    if(c1.size() < c2.size()) {
      for(Object o : c1) {
        if(c2.contains(o)) {
          size++;
        }
      }
      
    }
    else {
      for(Object o : c2) {
        if(c1.contains(o)) {
          size++;
        }
      }
    }
    return size;
  }
}
