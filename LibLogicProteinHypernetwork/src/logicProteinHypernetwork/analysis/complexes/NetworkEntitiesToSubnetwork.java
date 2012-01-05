/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes;

import edu.uci.ics.jung.graph.util.Pair;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Transforms a network entities into a protein subnetwork
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class NetworkEntitiesToSubnetwork implements Transformer<Iterable<NetworkEntity>, ProteinSubnetwork> {

  private boolean noSelfInteractions;
  protected ProteinSubnetwork ppin;
  private boolean addNeighbors;

  public NetworkEntitiesToSubnetwork() {
    this(null, false, false);
  }

  public NetworkEntitiesToSubnetwork(ProteinSubnetwork ppin, boolean noSelfInteractions, boolean addNeighbors) {
    this.noSelfInteractions = noSelfInteractions;
    this.ppin = ppin;
    this.addNeighbors = addNeighbors;
  }
  
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
    Set<Protein> vertices = new HashSet<Protein>();
    for (NetworkEntity e : entities) {
      if(e instanceof Protein) {
        vertices.add((Protein)e);
      }
      if (e instanceof Interaction && !exclude.contains(e)) {
        Interaction i = (Interaction) e;
        if (!graph.containsEdge(i)) {
          if (!noSelfInteractions || !i.isSelfInteraction()) {
            /** @todo find a more general place to remove self interactions for all algorithms that really need it */
            graph.addVertex(i.first().getProtein());
            graph.addVertex(i.second().getProtein());
            graph.addEdge(i, i.first().getProtein(), i.second().getProtein());
          }
        }
      }
    }
    if(addNeighbors) {
      for(Interaction i : ppin.getEdges()) {
        Pair<Protein> e = ppin.getEndpoints(i);
        boolean v1 = vertices.contains(e.getFirst());
        boolean v2 = vertices.contains(e.getSecond());
        if(!graph.containsEdge(i) && 
                ((v1 && !v2) || (!v1 && v2))) {
          graph.addVertex(e.getFirst());
          graph.addVertex(e.getSecond());
          graph.addEdge(i, e);
        }
      }
    }
    return graph;
  }
}
