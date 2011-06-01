/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.pis;

import java.util.Iterator;
import org.apache.commons.collections15.iterators.ArrayIterator;
import proteinHypernetwork.NetworkEntity;

/**
 * Class MultiPIS provides a container for the PIS of multiple proteins or
 * interactions.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MultiPIS extends PIS {

  private NetworkEntity[] entities;

  /**
   * Constructor of class MultiPIS.
   *
   * @param entities the network entities
   */
  public MultiPIS(NetworkEntity... entities) {
    this.entities = entities;
  }

  /**
   * Returns the network entities.
   *
   * @return the network entities
   */
  public NetworkEntity[] getEntities() {
    return entities;
  }

  @Override
  public String toString() {
    String s = "";
    for(int i = 0; i<entities.length; i++) {
      s += entities[i];
      if(i < entities.length - 1)
        s += ", ";
    }
    return s + ": " + getScore();
  }

  public Iterator<NetworkEntity> iterator() {
    return new ArrayIterator<NetworkEntity>(entities);
  }

}
