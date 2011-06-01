/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.pis;

import java.util.Iterator;
import proteinHypernetwork.NetworkEntity;

/**
 * Class SinglePIS provides a container for the PIS of a single network entity.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class SinglePIS extends PIS {

  private NetworkEntity entity;

  /**
   * Constructor of class SinglePIS.
   *
   * @param entity the network entity
   */
  public SinglePIS(NetworkEntity entity) {
    this.entity = entity;
  }

  /**
   * Returns the network entity.
   *
   * @return the network entity
   */
  public NetworkEntity getEntity() {
    return entity;
  }

  @Override
  public String toString() {
    return entity + ": " + getScore();
  }

  public Iterator<NetworkEntity> iterator() {
    return new Iterator<NetworkEntity>() {
      boolean hasNext = true;

      public boolean hasNext() {
        return hasNext;
      }

      public NetworkEntity next() {
        hasNext = false;
        return entity;
      }

      public void remove() {
        throw new UnsupportedOperationException("Not supported.");
      }
    };
  }
}
