/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

/**
 * Interface for an object that listens to changes of another.
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface Listener {
  public void update();
}
