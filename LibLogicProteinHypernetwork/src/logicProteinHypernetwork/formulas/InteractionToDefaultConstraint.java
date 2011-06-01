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
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Generates the default constraint for an interaction.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 * @deprecated 
 */
public class InteractionToDefaultConstraint implements Transformer<Interaction, Formula<NetworkEntity>> {

  /**
   * Generates the default constraint for an interaction.
   *
   * @param i the interaction
   * @return the default constraint
   */
  public Formula<NetworkEntity> transform(Interaction i) {
    FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();

    formula.openDisjunction();

    formula.negation();

    formula.literal(i);

    formula.openConjunction();
    for(Protein p : i.getProteins()) {
      formula.literal(p);
    }
    formula.close();

    formula.close();

    return formula.create();
  }

}
