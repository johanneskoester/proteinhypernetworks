/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import logicProteinHypernetwork.analysis.Processor;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;

import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;

/**
 * Class MinimalNetworkStates handles the computation of minimal network states
 * and a priory perturbed proteins or interactions.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MinimalNetworkStates extends Processor {

  private ProteinHypernetwork hypernetwork;
  private MultiMap<NetworkEntity, MinimalNetworkState> minimalNetworkStates;
  private Set<MinimalNetworkState> removed;
  private Set<NetworkEntity> possibleEntities;

  /**
   * Constructor of class MinimalNetworkStates.
   *
   * @param hypernetwork the hypernetwork
   */
  public MinimalNetworkStates(ProteinHypernetwork hypernetwork) {
    this.hypernetwork = hypernetwork;
    resetPerturbation();
  }

  /**
   * Constructs a new instance out of previous instance without recomputing
   * minimal network states. Perturbations are persisted.
   *
   * @param minimalNetworkStates a previous minimal network states instance
   */
  public MinimalNetworkStates(final MinimalNetworkStates minimalNetworkStates) {
    this(minimalNetworkStates.hypernetwork);
    this.minimalNetworkStates = minimalNetworkStates.minimalNetworkStates;
    this.removed = new HashSet<MinimalNetworkState>(minimalNetworkStates.removed);
    this.possibleEntities = new HashSet<NetworkEntity>(minimalNetworkStates.possibleEntities);
  }

  /**
   * Returns all not perturbed entities.
   *
   * @return all not perturbed entities
   */
  public Collection<NetworkEntity> getPossibleEntities() {
    // give back a copy to make thread save
    return new ArrayList<NetworkEntity>(possibleEntities);
  }

  /**
   * Returns true if an entity is possible / not perturbed.
   *
   * @param e the network entity
   * @return true if entity is possible
   */
  public boolean isPossible(NetworkEntity e) {
    return possibleEntities.contains(e);
  }

  /**
   * Returns true if a minimal network state for a network entity is present.
   *
   * @param e the network entity
   * @return true if a minimal network state is present
   */
  public boolean contains(NetworkEntity e) {
    return minimalNetworkStates.containsKey(e);
  }

  /**
   * Returns all possible minimal network states for a network entity.
   *
   * @param e the network entity
   * @return all minimal network states
   */
  public List<MinimalNetworkState> getMinimalNetworkStates(NetworkEntity e) {
    List<MinimalNetworkState> states = new ArrayList<MinimalNetworkState>(minimalNetworkStates.get(e));
    Iterator<MinimalNetworkState> ite = states.iterator();
    while(ite.hasNext()) {
      if(removed.contains(ite.next()))
        ite.remove();
    }
    return states;
  }

  public void process() {
    minimalNetworkStates =
            new MultiHashMap<NetworkEntity, MinimalNetworkState>();

    NetworkEntityToMinimalNetworkStates networkEntityToMinimalNetworkStates =
            new NetworkEntityToMinimalNetworkStates(hypernetwork, new ArrayList());

    int k = 0;
    int max = hypernetwork.getInteractions().size();
    for (NetworkEntity e : hypernetwork.getNetworkEntities()) {
      Collection<MinimalNetworkState> states = networkEntityToMinimalNetworkStates.transform(e);

      if (!states.isEmpty()) {
        minimalNetworkStates.putAll(e, states);
      } else {
        System.err.println("Error: Empty minimal network states without a perturbation!");
      }

      /*System.out.println("MinimalNetworkState " + minimalNetworkStates.size() +
              " of " + hypernetwork.getInteractions().size());*/
      progressBean.setProgress(++k, max);
    }

  }

  /**
   * Sets an a priori perturbation.
   *
   * @param perturbation the perturbed network entities
   */
  public void setPerturbation(final Collection<NetworkEntity> perturbation) {
    resetPerturbation();
    Set<NetworkEntity> affected = new HashSet<NetworkEntity>();

    for(NetworkEntity e : perturbation) {
      propagatePerturbation(e, affected);
    }

    for(NetworkEntity e : hypernetwork.getNetworkEntities()) {
      if(!affected.contains(e)) {
        possibleEntities.add(e);
      }
    }
  }

  /**
   * Reset the perturbation.
   */
  public final void resetPerturbation() {
    removed = new HashSet<MinimalNetworkState>();
    possibleEntities = new HashSet<NetworkEntity>(hypernetwork.getNetworkEntities().size());
  }

  /**
   * Propagate a perturbation effect to other minimal network states
   *
   * @param e the perturbed or affected interaction
   * @param affected the affected entities to be updated
   */
  private void propagatePerturbation(NetworkEntity e, Collection<NetworkEntity> affected) {
    affected.add(e);
    
    for (MinimalNetworkState m : new ArrayList<MinimalNetworkState>(minimalNetworkStates.values())) {
      if(!removed.contains(m)) {
        if (m.getNecessary().contains(e)) {
          removeMinimalNetworkState(m);

          if(getMinimalNetworkStates(m.getEntity()).isEmpty()) {
            propagatePerturbation(m.getEntity(), affected);
          }
        }
      }
    }
  }

  /**
   * Remove a minimal network state because it is impossible due to the a priori
   * perturbation.
   *
   * @param m the minimal network state
   */
  private final void removeMinimalNetworkState(MinimalNetworkState m) {
    removed.add(m);
  }
}
