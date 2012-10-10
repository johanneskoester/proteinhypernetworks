package proteinHypernetwork.graph;

import java.util.ArrayList;
import java.util.Collection;

import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class NetworkGraph extends UndirectedSparseMultigraph<Protein, Interaction> {
	
	public NetworkGraph(ProteinHypernetwork hypernetwork) {
		for(Protein p : hypernetwork.getProteins()) {
			addVertex(p);
		}
		
		for(Interaction i : hypernetwork.getInteractions()) {
			addEdge(i, i.getProteins());
		}
	}
	
	public Collection<Interaction> getEdges(Protein a, Protein b) {
		Collection<Interaction> interactions = new ArrayList<Interaction>();
		for(Interaction i : getIncidentEdges(a)) {
			if(i.contains(b)) {
				interactions.add(i);
			}
		}
		return interactions;
	}
}
