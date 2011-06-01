/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.formulas;

import java.util.Collection;
import modalLogic.formula.Formula;
import modalLogic.formula.factory.FormulaFactory;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;

/**
 * Generates a conjunction of a priori perturbations.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class PerturbationsToFormula implements Transformer<Collection<NetworkEntity>, Formula<NetworkEntity>> {

  /**
   * Returns a conjunction of negated network entity that models that these
   * are perturbed/impossible.
   * 
   * @param perturbations the perturbations
   * @return the conjunction
   */
  public Formula<NetworkEntity> transform(Collection<NetworkEntity> perturbations) {
    FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();

    formula.openConjunction();

    for(NetworkEntity e : perturbations) {
      formula.negation();
      formula.literal(e);
    }
    
    formula.close();

    return formula.create();
  }

}
