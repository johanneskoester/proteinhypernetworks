/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.formulas;

import modalLogic.formula.Formula;
import modalLogic.formula.factory.FormulaFactory;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.constraints.Constraint;

/**
 * Transforms all constraints into one conjunctive propositional logic formula.
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstraintsToFormula implements Transformer<ProteinHypernetwork, Formula<NetworkEntity>> {

  private Transformer<Constraint, Formula<NetworkEntity>> constraintToFormula = new ConstraintToFormula();

  /**
   * Transforms all constraints into one conjunction.
   *
   * @param hypernetwork the protein hypernetwork
   * @return the conjuction of constraints
   */
  public Formula<NetworkEntity> transform(ProteinHypernetwork hypernetwork) {
    FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();

    formula.openConjunction();

    for(Constraint c : hypernetwork.getConstraints()) {
      formula.subformula(constraintToFormula.transform(c));
    }
    
    formula.close();

    return formula.create();
  }

}
