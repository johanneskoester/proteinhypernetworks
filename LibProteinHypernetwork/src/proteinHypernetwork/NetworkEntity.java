/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork;

import proteinHypernetwork.util.Listenable;

/**
 * A network entity. Common interface for protein and interaction.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface NetworkEntity extends Listenable {

  /**
   * Returns the id of the network entity.
   * 
   * @return the id
   */
  public String getId();
}
