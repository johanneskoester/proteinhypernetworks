/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An object that allows listening on changes.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class AbstractListenable {

  private Collection<Listener> listeners = new ArrayList<Listener>();

  public void updateListeners() {
    for(Listener l : listeners)
      l.update();
  }

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  public Iterator<Listener> listenerIterator() {
    return listeners.iterator();
  }
}
