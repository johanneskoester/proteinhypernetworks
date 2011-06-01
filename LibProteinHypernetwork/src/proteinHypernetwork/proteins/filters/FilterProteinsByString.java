/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.proteins.filters;

import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.ParameterizedFilter;

/**
 * Filters proteins by a given String.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FilterProteinsByString extends ParameterizedFilter<Protein, String>{

  @Override
  public boolean compare(Protein o, String q) {
    return o.getId().startsWith(q);
  }

}
