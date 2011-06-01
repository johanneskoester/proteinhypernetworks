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
 * Filter interactions that have a protein in common with a given one.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterInteractionsByCommonProtein extends ParameterizedFilter<Interaction, Interaction> {

  @Override
  public boolean compare(Interaction o, Interaction q) {
    for(Interactor i : o) {
      for(Interactor j : q) {
        if(i.getProtein().compareTo(j.getProtein()) == 0)
          return true;
      }
    }
    return false;
  }

}
