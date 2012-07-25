/* Copyright (c) 2012, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.reactions;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Class ReactionsPrediction predicts kinetic reactions for the protein network
 * that do not violate constraints.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ReactionsPrediction extends Processor {

  private ProteinHypernetwork hypernetwork;
  private MinimalNetworkStates minimalNetworkStates;
  private UndirectedGraph<Protein, Interaction> network;
  private int depth;
  private int maxreactions;
  private List<Reaction> reactions;
  private Collection<Protein> proteins;

  /**
   * Constructor of class ReactionsPrediction.
   * 
   * @param hypernetwork the protein hypernetwork
   * @param mns all minimal network states
   * @param depth maximum depth when calculating reactions
   */
  public ReactionsPrediction(ProteinHypernetwork hypernetwork, int depth, int maxreactions) {
    this.hypernetwork = hypernetwork;
    this.depth = depth;
    this.maxreactions = maxreactions;
    this.network = hypernetwork.networkGraph();
  }

  public void calculateMinimalNetworkStates() {
    minimalNetworkStates = new MinimalNetworkStates(hypernetwork);
    minimalNetworkStates.listen(this);
    minimalNetworkStates.process();
  }

  /**
   * Returns the predicted reactions.
   * 
   * @return the reactions
   */
  public List<Reaction> getReactions() {
    return reactions;
  }

  /**
   * Sets the proteins to predict reactions for.
   * 
   * @param proteins the proteins
   */
  public void setProteins(Collection<Protein> proteins) {
    this.proteins = proteins;
  }

  public void process() {
    calculateMinimalNetworkStates();

    reactions = new ArrayList<Reaction>();
    for (Protein p : proteins) {
      reactions.add(new Source(p));
    }
    enumerateReactions(reactions, depth, maxreactions);
  }

  /**
   * Enumerates all reactions up to a given depth.
   * 
   * @param reactions the collection of reactions
   * @param depth the maximum depth
   */
  private void enumerateReactions(Collection<Reaction> reactions, int depth, int maxreactions) {
    int oldsize = 0;
    int oldoldsize = 0;
    for (int i = 0; i < depth; i++) {
      System.out.println(i);
      oldoldsize = oldsize;
      oldsize = reactions.size();
      List<Reaction> rs = new ArrayList<Reaction>(reactions);
      for (int k = 0; k < rs.size(); k++) {
        for (int l = Math.max(k, oldoldsize); l < rs.size(); l++) {
          //System.out.println(rs.get(k).product);
          //System.out.println(rs.get(l).product);
          enumerateReactions(rs.get(k).product, rs.get(l).product, reactions, maxreactions);
          if (reactions.size() == maxreactions) {
            return;
          }
        }
      }
      System.out.println("Reactions: " + reactions.size());
      progressBean.setProgress(i, depth);
      if (reactions.size() == oldsize) {
        System.out.println("No new reactions, stopping.");
        return;
      }
    }
    System.out.println("Stopping because maximum depth is reached.");
  }

  /**
   * Enumerate all reactions between two complexes.
   * 
   * @param a a complex
   * @param b a complex
   * @param reactions the updated reactions
   */
  private void enumerateReactions(ComplexMultigraph a, ComplexMultigraph b, Collection<Reaction> reactions, int maxreactions) {
    for (Protein p : a) {
      for (Protein q : b) {
        //System.out.println(a + " " + b);
        if (network.isNeighbor(p, q)) {
          for (Interaction i : network.getOutEdges(p)) {
            if (i.contains(q)) {
              // a candidate reaction
              for (MinimalNetworkState m : minimalNetworkStates.getMinimalNetworkStates(i)) {
                //System.out.println(i + "" + m);
                for (int vertexa : a.getVertices(p)) {
                  for (int vertexb : b.getVertices(q)) {
                    if (!a.isEdge(vertexa, q) && !b.isEdge(vertexb, p)) {
                      ComplexMultigraph product = new ComplexMultigraph(a);

                      try {
                        product.update(m, vertexa);

                        // a valid product candidate if no exception
                        product.merge(vertexa, i, vertexb, b);
                        Reaction r = new Reaction(a, b, product);
                        System.out.println(r);
                        reactions.add(r);
                        if (reactions.size() == maxreactions) {
                          System.out.println("Stopping because maximum number of reactions is exceeded.");
                          return;
                        }
                      } catch (ComplexMultigraph.NotConnectedException e) {
                        System.out.println("Not connected: " + a + " and " + b);
                        // ignore this complex
                      } catch (ComplexMultigraph.MissingEntityException e) {
                        System.out.println("Missing Entity: " + a + " and " + b);
                        // ignore this complex
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
