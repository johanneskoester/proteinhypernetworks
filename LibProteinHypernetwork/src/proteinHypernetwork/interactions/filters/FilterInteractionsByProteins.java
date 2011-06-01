/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.interactions.filters;

import edu.uci.ics.jung.graph.util.Pair;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filter all interactions that appear between a given pair of proteins.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterInteractionsByProteins extends ParameterizedFilter<Interaction, Pair<Protein>> {

  @Override
  public boolean compare(Interaction o, Pair<Protein> q) {
    for(Protein p : q) {
      boolean found = false;
      for (Interactor i : o) {
        if(i.getProtein().compareTo(p) == 0) {
          found = true;
          break;
        }
      }
      if(!found)
        return false;
    }
    return true;
  }
}
