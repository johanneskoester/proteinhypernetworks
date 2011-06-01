/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.interactions.filters;

import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filter all interactions with a certain protein.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterInteractionsByProtein extends ParameterizedFilter<Interaction, Protein> {

  @Override
  public boolean compare(Interaction o, Protein q) {
    for(Interactor i : o) {
          if(i.getProtein().compareTo(q) == 0)
            return true;
        }
        return false;
  }

}
