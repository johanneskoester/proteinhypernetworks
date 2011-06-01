/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.pis;

import java.util.Collection;
import proteinHypernetwork.NetworkEntity;

/**
 * Class MultiPIS provides a container for the PIS.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class PIS  implements Comparable, Iterable<NetworkEntity> {

  private float score;
  private Collection<NetworkEntity> affected;

  /**
   * Returns the due to the perturbation affected network entities.
   *
   * @return the affected network entities
   */
  public Collection<NetworkEntity> getAffected() {
    return affected;
  }

  /**
   * Sets the due to the perturbation affected network entities.
   *
   * @param affected the affected network entities
   */
  public void setAffected(Collection<NetworkEntity> affected) {
    this.affected = affected;
  }

  /**
   * Sets the perturbation impact score (PIS).
   *
   * @param score the PIS
   */
  public void setScore(float score) {
    this.score = score;
  }

  /**
   * Returns the PIS.
   *
   * @return the PIS
   */
  public float getScore() {
    return score;
  }
  
  public int compareTo(Object o) {
    if(o instanceof PIS) {
      float oScore = ((PIS)o).score;
      if(score > oScore)
        return 1;
      if(score < oScore)
        return -1;
      return 0;
    }
    return -1;
  }
}
