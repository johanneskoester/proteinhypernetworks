/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections15.collection.CompositeCollection;
import proteinHypernetwork.constraints.Constraints;
import proteinHypernetwork.exceptions.UnknownEntityException;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactions;
import proteinHypernetwork.proteins.Protein;
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
  
  public Collection<NetworkEntity> getNetworkEntities(Collection<String> ids) throws UnknownEntityException {
	  Collection<NetworkEntity> entities = new ArrayList<NetworkEntity>();
	  for(String id : ids) {
		  Protein p = proteins.getProteinById(id);
		  if(p != null)
			  entities.add(p);
		  else {
			  Collection<Interaction> is = interactions.getInteractionsWithId(id);
			  for(Interaction i : is) {
				  entities.add(i);
			  }
			  if(is.isEmpty())
				  throw new UnknownEntityException(id);
		  }
	  }
	  return entities;
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
  
  /**
   * Returns a graph for the whole protein network.
   * 
   * @return a graph for the whole protein network
   */
  public UndirectedGraph<Protein, Interaction> networkGraph() {
    UndirectedGraph<Protein, Interaction> graph = new UndirectedSparseGraph<Protein, Interaction>();
    for(Protein p : proteins) {
      graph.addVertex(p);
    }
    for(Interaction i : interactions) {
      graph.addEdge(i, i.getProteins());
    }
    return graph;
  }
}
