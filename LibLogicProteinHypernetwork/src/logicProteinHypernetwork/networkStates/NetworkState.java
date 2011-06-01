/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.networkStates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections15.iterators.IteratorChain;
import proteinHypernetwork.NetworkEntity;

/**
 * A network state is a collection of minimal network states.
 * Provides an interator over all necessary network entities in these.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkState implements Iterable<NetworkEntity> {
  private List<MinimalNetworkState> minimalNetworkStates = new ArrayList<MinimalNetworkState>();

  /**
   * Returns the contained minimal network states.
   *
   * @return the contained minimal network states
   */
  public List<MinimalNetworkState> getMinimalNetworkStates() {
    return minimalNetworkStates;
  }

  /**
   * Adds a minimal network state.
   *
   * @param s the minimal network state
   */
  public void add(MinimalNetworkState s) {
    minimalNetworkStates.add(s);
  }

  /**
   * Iterator over all necessary entities.
   * 
   * @return an iterator
   */
  public Iterator<NetworkEntity> iterator() {
    Iterator<NetworkEntity>[] iterators = new Iterator[minimalNetworkStates.size()];
    for(int i=0; i<minimalNetworkStates.size(); i++)
      iterators[i] = minimalNetworkStates.get(i).iterator();
    return new IteratorChain<NetworkEntity>(iterators);
  }

  /**
   * Iterator over all impossible entities.
   *
   * @return an iterator
   */
  public Iterator<NetworkEntity> impossiblesIterator() {
    Iterator<NetworkEntity>[] iterators = new Iterator[minimalNetworkStates.size()];
    for(int i=0; i<minimalNetworkStates.size(); i++)
      iterators[i] = minimalNetworkStates.get(i).getImpossible().iterator();
    return new IteratorChain<NetworkEntity>(iterators);
  }
}
