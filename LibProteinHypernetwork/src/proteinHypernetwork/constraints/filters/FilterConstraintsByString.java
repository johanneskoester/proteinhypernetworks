/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.constraints.filters;

import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.interactions.filters.FilterInteractionsByString;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filter constraints by a string.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterConstraintsByString extends ParameterizedFilter<Constraint, String> {
  private FilterInteractionsByString fi = new FilterInteractionsByString();

  @Override
  public boolean compare(Constraint o, String q) {
    return o.toString().contains(q);
  }
}
