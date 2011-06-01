/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.graphSummarization;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;

/**
 *
 * @author koester
 */
public class GraphSummarizationComplexPrediction implements Transformer<UndirectedGraph<Protein, Interaction>, Collection<Complex>> {

  private GraphSummarization<Protein, Interaction> gs = new GraphSummarization<Protein, Interaction>();

  public Collection<Complex> transform(UndirectedGraph<Protein, Interaction> g) {
    // ensure that each protein is its own neighbor as proposed in GS paper
    for (Protein p : g.getVertices()) {
      if (!g.isNeighbor(p, p)) {
        Interaction i = new Interaction();
        i.addInteractor(new Interactor(p));
        i.addInteractor(new Interactor(p));
        g.addEdge(i, i.getProteins());
      }
    }

    Collection<? extends Iterable<Protein>> cs = gs.graphSummarization(g);

    Collection<Complex> complexes = new ArrayList<Complex>(cs.size());
    for(Iterable<Protein> c : cs) {
      complexes.add(new Complex(c));
    }
    return complexes;
  }

}
