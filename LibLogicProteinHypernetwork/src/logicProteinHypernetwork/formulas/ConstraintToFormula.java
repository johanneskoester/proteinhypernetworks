/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.formulas;

import modalLogic.formula.Formula;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.constraints.Constraint;

/**
 * Transforms a constraint into a propositional logic formula.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstraintToFormula implements Transformer<Constraint, Formula<NetworkEntity>> {

  /**
   * Transforms a constraint into a propositional logic formula.
   *
   * @param c the constraint
   * @return the propositional logic formula
   */
  public Formula<NetworkEntity> transform(Constraint c) {
    Formula<NetworkEntity> formula = c.getImplication().clone();
    formula.toNegationNormalForm();
    return formula;
  }
}
