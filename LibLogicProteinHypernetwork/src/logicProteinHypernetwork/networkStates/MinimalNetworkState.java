/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.networkStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.SetUtils;
import proteinHypernetwork.NetworkEntity;

/**
 * Class MinimalNetworkState models a minimal network state for a particular 
 * network entity.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MinimalNetworkState implements Iterable<NetworkEntity> {
  private NetworkEntity entity;
  private Collection<NetworkEntity> necessary = new ArrayList<NetworkEntity>();
  private Collection<NetworkEntity> impossible = new ArrayList<NetworkEntity>();

  /**
   * Sets the network entity.
   *
   * @param entity the entity
   */
  public void setEntity(NetworkEntity entity) {
    this.entity = entity;
  }

  /**
   * Returns the network entity.
   *
   * @return the network entity
   */
  public NetworkEntity getEntity() {
    return entity;
  }

  /**
   * Adds a necessary network entity.
   *
   * @param e the network entity
   */
  public void addNecessary(NetworkEntity e) {
    necessary.add(e);
  }

  /**
   * Adds an impossible network entity.
   *
   * @param e the network entity
   */
  public void addImpossible(NetworkEntity e) {
    impossible.add(e);
  }

  /**
   * Returns the impossible network entities.
   *
   * @return the impossible network entities
   */
  public Collection<NetworkEntity> getImpossible() {
    return impossible;
  }

  /**
   * Returns the necessary network entities.
   * @return the necessary network entities
   */
  public Collection<NetworkEntity> getNecessary() {
    return necessary;
  }

  /**
   * Iterates over necessary network entities.
   *
   * @return an iterator
   */
  public Iterator<NetworkEntity> iterator() {
    return necessary.iterator();
  }

  /**
   * Returns true if two minimal network states are clashing, thus if their entities
   * cannot appear simultaneously if they are their only minimal network states.
   * 
   * @param s a minimal network state
   * @return whether they are clashing
   */
  public boolean isClash(MinimalNetworkState s) {
    return CollectionUtils.containsAny(necessary, s.impossible) || CollectionUtils.containsAny(impossible, s.necessary);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MinimalNetworkState other = (MinimalNetworkState) obj;

    return SetUtils.isEqualSet(necessary, other.necessary) && SetUtils.isEqualSet(impossible, other.impossible);
  }

  /**
   * A hash code ensuring consistency with equals.
   * 
   * @return int the hash code
   */
  @Override
  public int hashCode() {
    return SetUtils.hashCodeForSet(necessary) ^ SetUtils.hashCodeForSet(impossible);
  }

  @Override
  public String toString() {
    return "(" + necessary.toString() + "; " + impossible.toString() + ")";
  }
}
