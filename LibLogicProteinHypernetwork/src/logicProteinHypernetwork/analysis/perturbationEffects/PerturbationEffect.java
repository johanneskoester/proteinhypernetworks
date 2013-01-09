package logicProteinHypernetwork.analysis.perturbationEffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import proteinHypernetwork.NetworkEntity;

public class PerturbationEffect {

	private Collection<NetworkEntity> perturbations;
	private Map<NetworkEntity, Boolean> possibility = new HashMap<NetworkEntity, Boolean>();
	private Map<NetworkEntity, Float> competitors = new HashMap<NetworkEntity, Float>();
	private Map<NetworkEntity, Float> dependencies = new HashMap<NetworkEntity, Float>();
	private Map<NetworkEntity, Integer> mnscount = new HashMap<NetworkEntity, Integer>();
	
	public PerturbationEffect(Collection<NetworkEntity> perturbations) {
		super();
		this.perturbations = perturbations;
	}
	
	public Map<NetworkEntity, Boolean> getPossibility() {
		return possibility;
	}
	
	public Map<NetworkEntity, Float> getCompetitors() {
		return competitors;
	}
	
	public Map<NetworkEntity, Float> getDependencies() {
		return dependencies;
	}

	public Map<NetworkEntity, Integer> getMNSCount() {
		return mnscount;
	}
}
