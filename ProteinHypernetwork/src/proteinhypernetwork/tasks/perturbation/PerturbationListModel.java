/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetwork.tasks.perturbation;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;
import javax.swing.AbstractListModel;
import proteinHypernetwork.NetworkEntity;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class PerturbationListModel extends AbstractListModel {

  private Vector<NetworkEntity> entities;
  private Vector<NetworkEntity> showing;
  private Vector<NetworkEntity> searched;

  public void setEntities(Collection<NetworkEntity> entities) {
    this.entities = new Vector<NetworkEntity>(entities);
    showing = this.entities;
    searched = showing;
    fireContentsChanged(this, 0, searched.size());
  }

  @Override
  public int getSize() {
    if(searched == null)
      return 0;
    return searched.size();
  }

  @Override
  public Object getElementAt(int index) {
    return searched.get(index);
  }

  public void filter(Collection<NetworkEntity> c) {
    showing = new Vector<NetworkEntity>(entities);
    showing.retainAll(c);
    searched = showing;
    fireContentsChanged(this, 0, showing.size());
  }

  public boolean isFiltered() {
    return searched != showing;
  }

  public void filter(String e) {
    boolean invert = false;
    if (e.startsWith("!")) {
      e = e.substring(1);
      invert = true;
    }
    int oldSize;
    if(e.isEmpty()) {
      oldSize = searched.size();
      searched = showing;
    }
    else {
      oldSize = searched.size();
      searched = new Vector<NetworkEntity>();
      for(NetworkEntity s : showing) {
        if(!invert == s.toString().contains(e)) {
          searched.add(s);
        }
      }
    }

    fireContentsChanged(this, 0, oldSize-1); // force reload
  }
}
