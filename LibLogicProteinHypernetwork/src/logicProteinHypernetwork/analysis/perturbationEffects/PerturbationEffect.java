package logicProteinHypernetwork.analysis.perturbationEffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import proteinHypernetwork.NetworkEntity;

public class PerturbationEffect {

	private Collection<NetworkEntity> perturbations;
	private Map<NetworkEntity, Boolean> possibility = new HashMap<NetworkEntity, Boolean>();
	private Map<NetworkEntity, Integer> competitors = new HashMap<NetworkEntity, Integer>();
	private Map<NetworkEntity, Integer> dependencies = new HashMap<NetworkEntity, Integer>();
	public PerturbationEffect(Collection<NetworkEntity> perturbations) {
		super();
		this.perturbations = perturbations;
	}
	
	public Map<NetworkEntity, Boolean> getPossibility() {
		return possibility;
	}
	
	public Map<NetworkEntity, Integer> getCompetitors() {
		return competitors;
	}
	
	public Map<NetworkEntity, Integer> getDependencies() {
		return dependencies;
	}
}
