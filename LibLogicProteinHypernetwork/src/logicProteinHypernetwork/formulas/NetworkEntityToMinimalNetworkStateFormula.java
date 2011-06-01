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
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Generates a minimal network state formula for a network entity.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkEntityToMinimalNetworkStateFormula implements Transformer<NetworkEntity, Formula<NetworkEntity>> {
  
  private ProteinHypernetwork hypernetwork;
  private ConstraintToFormula constraintToFormula = new ConstraintToFormula();
  private Formula<NetworkEntity> constraints;
  private Formula<NetworkEntity> perturbations;


  /**
   * Constructor of class NetworkEntityToMinimalNetworkStateFormula.
   *
   * @param hypernetwork the hypernetwork
   * @param perturbations the perturbations
   */
  public NetworkEntityToMinimalNetworkStateFormula(ProteinHypernetwork hypernetwork, Collection<NetworkEntity> perturbations) {
    this.hypernetwork = hypernetwork;
    constraints = new ConstraintsToFormula().transform(hypernetwork);
    this.perturbations = new PerturbationsToFormula().transform(perturbations);
  }

  /**
   * Caution: Of multiple instances created by an instance of this class only the last one will be a valid formula
   * Thus: not thread safe!!!
   * 
   * @param e Interaction
   * @return the formula
   */
  public Formula<NetworkEntity> transform(NetworkEntity e) {
    FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();

    formula.openConjunction();

    formula.literal(e);

    if(e instanceof Interaction) {
      /*
       * Simulate the default constraint that enforces proteins of an interaction
       * to be possible simultaneously.
       */
      for(Protein p : ((Interaction)e).getProteins())
        formula.literal(p);
    }

    formula.subformula(perturbations);

    formula.subformula(constraints);

    formula.close();

    return formula.create();
  }
}
