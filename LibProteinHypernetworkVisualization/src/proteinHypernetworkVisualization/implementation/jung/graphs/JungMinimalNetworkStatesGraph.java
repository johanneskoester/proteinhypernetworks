/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinHypernetworkVisualization.implementation.jung.graphs;


import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetworkVisualization.graphs.MinimalNetworkStatesGraph;
import proteinHypernetworkVisualization.implementation.jung.graphs.JungMinimalNetworkStatesGraph.TypedEntity;

/**
 *
 * @author koester
 */
public class JungMinimalNetworkStatesGraph extends UndirectedSparseGraph<TypedEntity, TypedEntity> implements MinimalNetworkStatesGraph {

  public void setMinimalNetworkStates(Collection<MinimalNetworkState> states) {
    Map<Protein, TypedEntity> proteins = new HashMap<Protein, TypedEntity>();
    for (MinimalNetworkState state : states) {
      for (NetworkEntity e : state.getNecessary()) {
        if (e instanceof Protein) {
          TypedEntity f = new TypedEntity(e, EntityType.NECESSARY);
          addVertex(f);
          proteins.put((Protein) e, f);
        }
      }

      for (NetworkEntity e : state.getImpossible()) {
        if (e instanceof Protein) {
          TypedEntity f = new TypedEntity(e, EntityType.IMPOSSIBLE);
          addVertex(f);
          proteins.put((Protein) e, f);
        }
      }
    }

    for (MinimalNetworkState state : states) {
      for (NetworkEntity e : state.getNecessary()) {
        if (e instanceof Interaction) {
          Interaction i = (Interaction) e;
          addEdge(new TypedEntity(e, EntityType.NECESSARY),
                  new Pair<TypedEntity>(proteins.get(i.first().getProtein()), proteins.get(i.second().getProtein())));
        }
      }

      for (NetworkEntity e : state.getImpossible()) {
        if (e instanceof Interaction) {
          Interaction i = (Interaction) e;
          addEdge(new TypedEntity(e, EntityType.IMPOSSIBLE),
                  new Pair<TypedEntity>(proteins.get(i.first().getProtein()), proteins.get(i.second().getProtein())));
        }
      }
    }
  }

  public class TypedEntity {

    private NetworkEntity e;
    private EntityType type;

    public TypedEntity(NetworkEntity e, EntityType type) {
      this.e = e;
      this.type = type;
    }

    public NetworkEntity getNetworkEntity() {
      return e;
    }

    public EntityType getType() {
      return type;
    }
  }

  public enum EntityType {

    NECESSARY, IMPOSSIBLE
  }
}
