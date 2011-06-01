/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.interactions.filters;

import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.util.Filter;

/**
 * Filter all network entities that are interactions.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterNetworkEntitiesToInteractions extends Filter<NetworkEntity> {

  @Override
  public boolean compare(NetworkEntity o) {
    return o instanceof Interaction;
  }

}
