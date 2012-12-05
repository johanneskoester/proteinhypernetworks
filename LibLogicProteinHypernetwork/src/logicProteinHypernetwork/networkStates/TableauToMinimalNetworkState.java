/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.networkStates;

import modalLogic.formula.Formula;
import modalLogic.tableau.LabelledFormula;
import modalLogic.tableau.Tableau;
import modalLogic.tableau.World;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;

/**
 * Extracts a minimal network state out of a tableau that contains a minimally
 * constrained satisfying model (Köster, Zamir, Rahmann, 2011).
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class TableauToMinimalNetworkState implements Transformer<Tableau<NetworkEntity>, MinimalNetworkState>{

  /**
   * Returns a minimal network state out of a finished tableau.
   *
   * @param t the tableau
   * @return the minimal network state
   */
  public MinimalNetworkState transform(Tableau<NetworkEntity> t) {
    World<NetworkEntity> w = t.getWorlds().getStart();

    MinimalNetworkState s = new MinimalNetworkState();
    for(LabelledFormula<NetworkEntity> l : w.getPositiveLiterals()) {
      s.addNecessary(l.getFormula().getProposition());
    }

    for(LabelledFormula<NetworkEntity> l : w.getNegativeLiterals()) {
      Formula<NetworkEntity> f = l.getFormula();
      if(f.getParent().getType() != Formula.DISJUNCTION ||
              f.getParent().indexOf(f) != 0)
        s.addImpossible(f.getProposition());
    }

    return s;
  }

}
