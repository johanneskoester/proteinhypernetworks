/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

/**
 * An interface for an object that allows listening on changes.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface Listenable {
  public void updateListeners();

  public void addListener(Listener listener);

  public void removeListener(Listener listener);
}
