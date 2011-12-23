/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package util.lcma;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

/**
 * Fast implementation of the LCM algorithm (Li et al., 2005)
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FastLCMA<V, E, C extends Collection<V>>  implements Transformer<UndirectedGraph<V, E>, Collection<C>> {

  private double similarityThreshold;
  private double densityLoss;
  private Factory<C> complexFactory;
  private int additionalIteration = 0;
  private double minDensity = 0.2;

  /**
   * Constructor of class FastLCMA.
   *
   * @param similarityThreshold the similarity threshold for merging cliques
   * @param densityLoss the maximum loss of density in each iteration
   * @param complexFactory factory to create complexes
   */
  public FastLCMA(double similarityThreshold, double densityLoss, Factory<C> complexFactory) {
    this.similarityThreshold = similarityThreshold;
    this.densityLoss = densityLoss;
    this.complexFactory = complexFactory;
  }

  /**
   * Constructor of class FastLCMA.
   *
   * @param similarityThreshold the similarity threshold for merging cliques
   * @param complexFactory factory to create complexes
   */
  public FastLCMA(double similarityThreshold, Factory<C> complexFactory) {
    this(similarityThreshold, 0.95, complexFactory);
  }

  /**
   * Set the merge similarity threshold
   * @param similarityThreshold the similarity threshold
   */
  public void setSimilarityThreshold(double similarityThreshold) {
    this.similarityThreshold = similarityThreshold;
  }

  /**
   * Returns the similarity threshold.
   *
   * @return the similarity threshold
   */
  public double getSimilarityThreshold() {
    return similarityThreshold;
  }

  /**
   * Sets the number of additional iterations once density falls below densityLoss.
   *
   * @param additionalIteration number of additional iterations
   */
  public void setAdditionalIteration(int additionalIteration) {
    this.additionalIteration = additionalIteration;
  }

  /**
   * Transform an undirected loop free(!) graph to a collection of dense complexes.
   *
   * @param g the graph
   * @return collection of complexes
   */
  public Collection<C> transform(UndirectedGraph<V, E> g) {
    return generateComplexes(transformToSubgraphs(g));
  }

  /**
   * Create dense subgraphs from undirected loop free graph.
   *
   * @param g the graph
   * @return the dense subgraphs
   */
  public Set<Subgraph<V>> transformToSubgraphs(UndirectedGraph<V, E> g) {
    Set<Subgraph<V>> subgraphs = mergeCliques(findLocalCliques(g));
    return subgraphs;
  }

  /**
   * Generates complexes from subgraphs.
   *
   * @param subgraphs the subgraphs
   * @return the complexes
   */
  protected Collection<C> generateComplexes(Iterable<Subgraph<V>> subgraphs) {
    Collection<C> complexes = new ArrayList<C>();
    for (Subgraph<V> s : subgraphs) {
      C complex = complexFactory.create();
      for (V v : s) {
        complex.add(v);
      }
      complexes.add(complex);
    }

    return complexes;
  }

  /**
   * Find local cliques by removing node with lowest degree in local neighbourhood
   * of each node.
   *
   * @param graph the graph
   * @return local cliques as subgraphs
   */
  private Set<Subgraph<V>> findLocalCliques(UndirectedGraph<V, E> graph) {
    Set<Subgraph<V>> cliques = new HashSet<Subgraph<V>>();

    for (V v : graph.getVertices()) {

      LocalNeighbourhoodGraph<V, E> lng = new LocalNeighbourhoodGraph<V, E>(v, graph);

      boolean stop = false;
      while (!stop) {

        V min = getMinDegreeVertex(lng);

        if (lng.densityWithoutVertex(min) > lng.density()) {
          lng.removeVertex(min);
        } else {
          stop = true;
        }
      }

      if (lng.density() < 1) {
        throw new RuntimeException("LCMA: LNG density not one after first step.");
        /*System.err.println(lng.density());
        Set<V> vs = new HashSet<V>();
        for (V x : lng) {
          vs.add(x);
        }
        for (V x : lng) {
          for (E e : graph.getIncidentEdges(x)) {
            if (vs.containsAll(graph.getEndpoints(e))) {
              System.err.println(e);
            }
          }
        }*/
      }

      if (lng.getVertexCount() > 2) {

        cliques.add(new Clique<V, E>(lng));
      }
    }

    return cliques;
  }

  /**
   * Merge cliques if they have more than similarityThreshold overlap.
   *
   * @param cliques the cliques
   * @return merged cliques that are still dense enough
   */
  private Set<Subgraph<V>> mergeCliques(Set<Subgraph<V>> cliques) {
    int addIte = additionalIteration;

    ArrayList<Subgraph<V>> denseSubgraphs = new ArrayList<Subgraph<V>>(cliques);
    double avgDensity = 1; // since all input subgraphs are cliques
    while (true) {
      Set<Subgraph<V>> merged = new HashSet<Subgraph<V>>();
      boolean anyMerge = false;

      boolean[] mergeStatus = new boolean[denseSubgraphs.size()];

      for (int i = 0; i < denseSubgraphs.size(); i++) {
        boolean tempMerge = false;
        Subgraph<V> s = denseSubgraphs.get(i);
        MergedCliques<V, E> ds = new MergedCliques<V, E>(s);
        for (int j = 0; j < denseSubgraphs.size(); j++) {
          if (i != j) {
            Subgraph<V> s2 = denseSubgraphs.get(j);
            if (s.significantOverlap(s2, similarityThreshold)) {
              ds.merge(s2);
              mergeStatus[i] = true;
              mergeStatus[j] = true;
              anyMerge = true;
              tempMerge = true;
            }
          }
        }
        if (tempMerge) {
          merged.add(ds);
        }
      }

      for (int i = 0; i < denseSubgraphs.size(); i++) {
        if (!mergeStatus[i]) {
          merged.add(denseSubgraphs.get(i));
        }
      }

      if (!anyMerge) {
        return merged;
      } else {
        double navgDensity = avgDensity(merged);
        if (navgDensity > densityLoss * avgDensity || addIte > 0) {
          avgDensity = navgDensity;
          denseSubgraphs = new ArrayList<Subgraph<V>>(merged);
          addIte--;
        } else {
          return merged;
        }
      }
    }
  }

  /**
   * Calculate the average density of given subgraphs.
   *
   * @param subgraphs the subgraphs
   * @return the average density
   */
  public double avgDensity(Collection<Subgraph<V>> subgraphs) {
    double avg = 0;
    for (Subgraph<V> s : subgraphs) {
      avg += s.density();
    }
    avg /= subgraphs.size();
    return avg;
  }

  /**
   * Returns the vertex with minimum degree in a local neighbourhood graph.
   *
   * @param lng the local neighbourhood graph
   * @return the vertex with minimum degree
   */
  private V getMinDegreeVertex(LocalNeighbourhoodGraph<V, E> lng) {
    for (V v : lng) {
      if (v != lng.getVertex()) {
        return v;
      }
    }
    return lng.getVertex(); // return the constituting vertex as a last resort
  }
}
