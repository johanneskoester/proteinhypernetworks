/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package hypernetwork.constraints;

import java.util.Iterator;
import modalLogic.formula.factory.FormulaFactory;
import org.apache.commons.collections15.iterators.ArrayIterator;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.interactions.Interaction;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Competition implements ConstraintTemplate {

  private Constraint[] constraint = new Constraint[2];

  public Competition(Interaction i1, Interaction i2) {
    if(! i1.intersects(i2))
      throw new UnsupportedOperationException(
              "The two interactions of a competition have to share a protein.");

    Interaction[] is = {i1, i2};
    for(int k=0; k<2; k++) {
      constraint[k] = new Constraint();
      FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();
      formula.openImplication();
      formula.literal(is[k]);
      formula.negation();
      formula.literal(is[(k+1)%2]);
      formula.close();
      constraint[k].setImplication(formula.create());
    }
  }
  
  @Override
  public Iterator<Constraint> iterator() {
    return new ArrayIterator<Constraint>(constraint);
  }

}
