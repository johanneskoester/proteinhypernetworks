/* Copyright (c) 2012, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.reactions;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexMultigraph extends UndirectedSparseGraph<Integer, Integer> implements Iterable<Protein> {

  private Map<Integer, Protein> verticesToProteins = new HashMap<Integer, Protein>();
  private MultiMap<Protein, Integer> proteinsToVertices = new MultiHashMap<Protein, Integer>();
  private Map<Integer, Interaction> edgesToInteractions = new HashMap<Integer, Interaction>();
  private MultiMap<Interaction, Integer> interactionToEdges = new MultiHashMap<Interaction, Integer>();

  public ComplexMultigraph(List<Protein> proteins, UndirectedGraph<Protein, Interaction> network) {
    for (int i = 0; i < proteins.size(); i++) {
      addVertex(i);
      verticesToProteins.put(i, proteins.get(i));
      proteinsToVertices.put(proteins.get(i), i);
    }

    for (int i = 0; i < proteins.size(); i++) {
      Protein p = verticesToProteins.get(i);
      for (Interaction interaction : network.getOutEdges(p)) {
        Protein q = interaction.getOtherProtein(p);
        for (int j : proteinsToVertices.get(q)) {
          int edgeid = edgesToInteractions.size();
          edgesToInteractions.put(edgeid, interaction);
          interactionToEdges.put(interaction, edgeid);
          addEdge(edgeid, i, j);
        }
      }
    }
  }

  public ComplexMultigraph(ComplexMultigraph cmg) {
    this.proteinsToVertices = new MultiHashMap<Protein, Integer>(cmg.proteinsToVertices);
    this.interactionToEdges = new MultiHashMap<Interaction, Integer>(cmg.interactionToEdges);
    this.verticesToProteins = new HashMap<Integer, Protein>(cmg.verticesToProteins);
    this.edgesToInteractions = new HashMap<Integer, Interaction>(cmg.edgesToInteractions);

    for (int v : cmg.getVertices()) {
      addVertex(v);
    }

    for (int e : cmg.getEdges()) {
      addEdge(e, cmg.getEndpoints(e));
    }
  }

  public ComplexMultigraph(Protein p) {
    addVertex(0);
    proteinsToVertices.put(p, 0);
    verticesToProteins.put(0, p);
  }
  
  public Collection<Integer> getVertices(Protein p) {
    return proteinsToVertices.get(p);
  }
  
  public void merge(int vertex, Interaction i, int p, ComplexMultigraph m) {
    
    Map<Integer, Integer> pidmap = new HashMap<Integer, Integer>();
    for(Entry<Integer, Protein> e : m.verticesToProteins.entrySet()) {
      int pid = getVertexCount();
      addVertex(pid);
      pidmap.put(e.getKey(), pid);
      proteinsToVertices.put(e.getValue(), pid);
      verticesToProteins.put(pid, e.getValue());
    }
    
    for(Entry<Integer, Interaction> e : m.edgesToInteractions.entrySet()) {
      int eid = getEdgeCount();
      Pair<Integer> endp = m.getEndpoints(e.getKey());
      addEdge(eid, pidmap.get(endp.getFirst()), pidmap.get(endp.getSecond()));
      edgesToInteractions.put(eid, e.getValue());
      interactionToEdges.put(e.getValue(), eid);
    }
    
    int eid = getEdgeCount();
    addEdge(eid, pidmap.get(p), vertex);
    interactionToEdges.put(i, eid);
    edgesToInteractions.put(eid, i);
  }

  public boolean isConnected() {
    return bfs().size() == getVertexCount();
  }

  public void subtract(MinimalNetworkState m, int vertex) throws NotConnectedException {
    Set<Interaction> impossible = new HashSet<Interaction>();

    for (NetworkEntity entity : m.getImpossible()) {
      if (entity instanceof Protein) {
        for (int v : proteinsToVertices.remove((Protein) entity)) {
          removeVertex(v);
          verticesToProteins.remove(v);
        }
      } else if (entity instanceof Interaction) {
        impossible.add((Interaction) entity);
      }
    }

    subtract(impossible, vertex);
  }

  private void subtract(Set<Interaction> impossible, int vertex) throws NotConnectedException {
    Set<Integer> visited = new HashSet<Integer>();
    Deque<Integer> todo = new ArrayDeque<Integer>();

    todo.add(vertex);
    while (!todo.isEmpty()) {
      int u = todo.remove();
      visited.add(u);


      for (int e : getIncidentEdges(u)) {
        Interaction i = edgesToInteractions.get(e);
        if (impossible.contains(i)) {
          removeEdge(e);
          edgesToInteractions.remove(e);
          interactionToEdges.remove(i, e);
          impossible.remove(i);
        }
      }

      for (int v : getNeighbors(u)) {
        todo.add(v);
      }
    }

    if (visited.size() < getVertexCount()) {
      throw new NotConnectedException();
    }
  }

  public Set<Integer> bfs() {
    Set<Integer> visited = new HashSet<Integer>();
    Deque<Integer> todo = new ArrayDeque<Integer>();

    todo.add(0);
    while (!todo.isEmpty()) {
      int u = todo.remove();
      visited.add(u);

      for (int v : getNeighbors(u)) {
        todo.add(v);
      }
    }
    return visited;
  }

  public Iterator<Protein> iterator() {
    return new Iterator<Protein>() {

      private Iterator<Entry<Integer, Protein>> entries = verticesToProteins.entrySet().iterator();

      public boolean hasNext() {
        return entries.hasNext();
      }

      public Protein next() {
        return entries.next().getValue();
      }

      public void remove() {
        throw new UnsupportedOperationException("Not supported.");
      }
    };
  }
  private String repr = null;

  @Override
  public String toString() {
    if (repr == null) {
      repr = "";
      List<Protein> proteins = new ArrayList<Protein>(verticesToProteins.values());
      Collections.sort(proteins);
      for (Protein p : proteins) {
        repr += p;
      }
    }
    return repr;
  }

  public class NotConnectedException extends Exception {
  }

  public class MissingEntityException extends Exception {
  }
}
