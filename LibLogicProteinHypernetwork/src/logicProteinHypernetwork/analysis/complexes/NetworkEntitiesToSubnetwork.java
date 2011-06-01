/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis.complexes;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;

/**
 * Transforms a network entities into a protein subnetwork
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkEntitiesToSubnetwork implements Transformer<Iterable<NetworkEntity>, ProteinSubnetwork> {

  /**
   * Returns protein subnetwork for given network entities.
   * 
   * @param entities the network entities
   * @return the protein subnetwork
   */
  public ProteinSubnetwork transform(Iterable<NetworkEntity> entities) {
    return transform(entities, new HashSet<NetworkEntity>());
  }


  /**
   * Returns protein subnetwork for given network entities while excluding others.
   *
   * @param entities the network entities
   * @param exclude network entities to exclude
   * @return the protein subnetwork
   */
  public ProteinSubnetwork transform(Iterable<NetworkEntity> entities, Set<NetworkEntity> exclude) {
    ProteinSubnetwork graph = new ProteinSubnetwork();
    for(NetworkEntity e : entities) {
      if(e instanceof Interaction && !exclude.contains(e)) {
        Interaction i = (Interaction)e;
        if(!graph.containsEdge(i)) {
          if(!i.isSelfInteraction()) {
            /** @todo find a more general place to remove self interactions for all algorithms that really need it */
            graph.addVertex(i.first().getProtein());
            graph.addVertex(i.second().getProtein());
            graph.addEdge(i, i.first().getProtein(), i.second().getProtein());
          }
        }
      }
    }
    return graph;
  }

}
