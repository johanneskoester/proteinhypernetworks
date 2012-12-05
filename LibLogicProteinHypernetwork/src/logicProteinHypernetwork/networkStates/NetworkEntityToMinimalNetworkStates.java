/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import modalLogic.formula.Formula;
import modalLogic.tableau.KRules;
import modalLogic.tableau.LabelledFormula;
import modalLogic.tableau.Rules;
import modalLogic.tableau.Tableau;
import modalLogic.tableau.World;
import modalLogic.tableau.comparators.PointerComparator;
import logicProteinHypernetwork.formulas.NetworkEntityToMinimalNetworkStateFormula;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Calculates minimal network states for a network entity.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkEntityToMinimalNetworkStates implements Transformer<NetworkEntity, Collection<MinimalNetworkState>> {

  private ProteinHypernetwork hypernetwork;
  private Rules<NetworkEntity> tableauRules = new KRules<NetworkEntity>();
  private PointerComparator<NetworkEntity> propositionComparator = new PointerComparator<NetworkEntity>();
  private TableauToMinimalNetworkState ts = new TableauToMinimalNetworkState();
  private NetworkEntityToMinimalNetworkStateFormula networkEntityToFormula;

  /**
   * Constructor of class NetworkEntityToMinimalNetworkStates.
   *
   * @param hypernetwork the hypernetwork
   * @param perturbations the perturbations
   */
  public NetworkEntityToMinimalNetworkStates(ProteinHypernetwork hypernetwork, Collection<NetworkEntity> perturbations) {
    this.hypernetwork = hypernetwork;
    networkEntityToFormula = new NetworkEntityToMinimalNetworkStateFormula(hypernetwork, perturbations);
  }

  /**
   * Returns all minimal network states for a given entity.
   *
   * @param e the network entity
   * @return all minimal network states
   */
  public Collection<MinimalNetworkState> transform(final NetworkEntity e) {
    Collection<MinimalNetworkState> states = new ArrayList<MinimalNetworkState>();
    Formula<NetworkEntity> f = networkEntityToFormula.transform(e);
    calculateAllTableauPaths(e, f, states);
    return states;
  }

  /**
   * Calculates all minimally constrained satisfying models using the tableau
   * algorithm and derives the minimal network states.
   *
   * @param e the network entity
   * @param f the minimal network state formula
   * @param states the place where all minimal network states are collected
   */
  public void calculateAllTableauPaths(final NetworkEntity e, final Formula<NetworkEntity> f, final Collection<MinimalNetworkState> states) {
    Stack<Collection<Formula<NetworkEntity>>> blocked = new Stack<Collection<Formula<NetworkEntity>>>();
    blocked.push(new ArrayList<Formula<NetworkEntity>>());
    boolean first = true;
    while (!blocked.isEmpty()) {
      Tableau<NetworkEntity> tableau = new Tableau<NetworkEntity>(tableauRules, propositionComparator, false);
      tableau.setFormula(f);

      Collection<Formula<NetworkEntity>> bs = blocked.pop();
      World start = new World(0, propositionComparator);
      for (Formula<NetworkEntity> b : bs) {
        tableau.block(start, b);
      }

      boolean satisfiable = tableau.proofSearch();
      if(!satisfiable && first) {
    	  System.err.println("Formula not satisfiable for entity " + e + ". This indicates a circular interaction dependency which is not allowed. Until you fix this, we assume that the entity does not have any competitors or dependencies.");
    	  MinimalNetworkState mns = new MinimalNetworkState();
          mns.addNecessary(e);
          if(e instanceof Interaction) {
          	for(Protein p : ((Interaction)e).getProteins())
          		mns.addNecessary(p);
          }
          states.add(mns);
    	  
      }
      if (satisfiable) {
        MinimalNetworkState s = ts.transform(tableau);
        s.setEntity(e);
        states.add(s);

        for (LabelledFormula<NetworkEntity> d : tableau.getDisjunctions()) {
          Formula<NetworkEntity> active = tableau.getActiveDisjunct(d).getFormula();
          int index = active.getParent().indexOf(active);
          if (index != 0 && index < active.getParent().getChildCount()-1) {
            Collection<Formula<NetworkEntity>> bs2 = new ArrayList<Formula<NetworkEntity>>(bs);
            bs2.add(tableau.getActiveDisjunct(d).getFormula());
            blocked.push(bs2);
          }
        }
      }
      first = false;
    }
  }
}
