/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.constraints.filters;

import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filter constraints by an interaction.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterConstraintsByInteraction extends ParameterizedFilter<Constraint, Interaction> {

  @Override
  public boolean compare(Constraint o, Interaction q) {
    for(NetworkEntity e : o.getImplication().getPropositions()) {
      if(e.equals(q))
        return true;
    }
    return false;
  }
}
