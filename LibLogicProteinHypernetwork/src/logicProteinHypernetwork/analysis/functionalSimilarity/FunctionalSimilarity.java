/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.functionalSimilarity;

import proteinHypernetwork.NetworkEntity;

/**
 * Describes predicted functional similarity between two network entities.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FunctionalSimilarity implements Comparable {

  private NetworkEntity entity1, entity2;
  private float similarity;

  /**
   * Constructor of class FunctionalSimilarity.
   *
   * @param entity1 an entity
   * @param entity2 an entity
   * @param similarity their similarity
   */
  public FunctionalSimilarity(NetworkEntity entity1, NetworkEntity entity2, float similarity) {
    this.entity1 = entity1;
    this.entity2 = entity2;
    this.similarity = similarity;
  }

  /**
   * Returns the functional similarity.
   *
   * @return the similarity
   */
  public float getSimilarity() {
    return similarity;
  }

  /**
   * Sets the functional similarity.
   * 
   * @param similarity the similarity
   */
  public void setSimilarity(float similarity) {
    this.similarity = similarity;
  }

  @Override
  public String toString() {
    return entity1 + "\t" + entity2 + "\t" + similarity;
  }

  public int compareTo(Object o) {
    if(o instanceof FunctionalSimilarity) {
      float oSimilarity = ((FunctionalSimilarity)o).similarity;
      if(similarity > oSimilarity)
        return 1;
      if(similarity < oSimilarity)
        return -1;
      return 0;
    }
    return -1;
  }
}
