/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork;

import java.util.Collection;
import org.apache.commons.collections15.collection.CompositeCollection;
import proteinHypernetwork.constraints.Constraints;
import proteinHypernetwork.interactions.Interactions;
import proteinHypernetwork.proteins.Proteins;

/**
 * A protein hypernetwork, consisting of proteins, interactions and constraints.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinHypernetwork {

  private Proteins proteins = new Proteins(this);
  private Interactions interactions = new Interactions(this);
  private Constraints constraints = new Constraints(this);
  private CompositeCollection<NetworkEntity> networkEntities;

  /**
   * Constructor of class ProteinHypernetwork.
   */
  public ProteinHypernetwork() {
    networkEntities = new CompositeCollection<NetworkEntity>(new Collection[]{proteins, interactions});
  }

  /**
   * Check whether hypernetwork is empty.
   * 
   * @return true if hypernetwork is empty
   */
  public boolean isEmpty() {
    return proteins.isEmpty();
  }

  /**
   * Returns the proteins.
   *
   * @return the proteins
   */
  public Proteins getProteins() {
    return proteins;
  }

  /**
   * Returns the interactions.
   *
   * @return the interactions
   */
  public Interactions getInteractions() {
    return interactions;
  }

  /**
   * Returns the constraints.
   *
   * @return the constraints
   */
  public Constraints getConstraints() {
    return constraints;
  }

  /**
   * Returns all network entities.
   *
   * @return the network entities
   */
  public Collection<NetworkEntity> getNetworkEntities() {
    return networkEntities;
  }
}
