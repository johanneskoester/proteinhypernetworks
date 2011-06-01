/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.interactions.filters;

import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filter all interactions with a certain interactor.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterInteractionsByInteractor extends ParameterizedFilter<Interaction, Interactor> {

  @Override
  public boolean compare(Interaction o, Interactor q) {
    return o.contains(q);
  }

}
