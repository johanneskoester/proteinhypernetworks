/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.constraints;

import java.util.ArrayList;
import modalLogic.formula.Formula;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * A list of constraints.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Constraints extends ArrayList<Constraint> {

  private ProteinHypernetwork hypernetwork;

  /**
   * Constructor of class Constraints.
   *
   * @param hypernetwork the protein hypernetwork
   */
  public Constraints(ProteinHypernetwork hypernetwork) {
    this.hypernetwork = hypernetwork;
  }

  /**
   * Returns the index of the inverse constraint of a given one if available.
   *
   * @param c the constraint
   * @return the index of the inverse constraint
   */
  public int getInverseIndex(Constraint c) {
    Formula<NetworkEntity> implication = c.getImplication();
    NetworkEntity constraining = implication.getChild(1).getProposition();
    NetworkEntity constrained = c.getConstrainedNetworkEntity();
    for(int j = 0; j<this.size(); j++) {
      Constraint d = this.get(j);
      Formula<NetworkEntity> i = d.getImplication();
      if(d.getConstrainedNetworkEntity().equals(constraining) && i.getChild(1).getProposition().equals(constrained))
        return j;
    }
    return -1;
  }
}
