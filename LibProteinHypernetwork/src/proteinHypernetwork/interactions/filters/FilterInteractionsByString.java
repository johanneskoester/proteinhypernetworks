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
 * Filter all interactions starting with a certain String representation.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterInteractionsByString extends ParameterizedFilter<Interaction, String>{


  @Override
  public boolean compare(Interaction o, String q) {
    for(Interactor i : o) {
          if(i.getProtein().getId().startsWith(q))
            return true;
        }
        return false;
  }

}
