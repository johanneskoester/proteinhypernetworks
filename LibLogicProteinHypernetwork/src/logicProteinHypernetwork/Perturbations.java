/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import proteinHypernetwork.NetworkEntity;

/**
 * Manages perturbation state of each protein and interaction.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Perturbations {

  private Set<NetworkEntity> perturbations = new HashSet<NetworkEntity>();

  /**
   * Returns all perturbed network entities.
   * 
   * @return all perturbed network entities
   */
  public Collection<NetworkEntity> getPerturbations() {
    return perturbations;
  }

  /**
   * Perturbs a network entity.
   *
   * @param e the network entity
   */
  public void perturbation(NetworkEntity e) {
    perturbations.add(e);
  }

  /**
   * Undo the perturbation of a network entity.
   *
   * @param e the network entity
   */
  public void undoPerturbation(NetworkEntity e) {
    perturbations.remove(e);
  }
}
