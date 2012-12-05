package logicProteinHypernetwork.analysis.perturbationEffects;

import java.util.ArrayList;
import java.util.Collection;

import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;
import logicProteinHypernetwork.networkStates.MinimalNetworkStates;

public class PerturbationEffectPrediction extends Processor {

	private ProteinHypernetwork hypernetwork;
	private MinimalNetworkStates mns;
	private PerturbationEffect effect;
	private Collection<NetworkEntity> perturb;
	
	public PerturbationEffectPrediction(Collection<NetworkEntity> perturb, ProteinHypernetwork hypernetwork) {
		this.perturb = perturb;
		this.hypernetwork = hypernetwork;
		mns = new MinimalNetworkStates(hypernetwork);
	}
	
	public PerturbationEffect getEffect() {
		return effect;
	}

	@Override
	public void process() {
		mns.process();
		estimateEffect(perturb);
	}
	
	private void estimateEffect(Collection<NetworkEntity> entities) {
		mns.setPerturbation(entities);
		
		PerturbationEffect pe = new PerturbationEffect(entities);
		
		for(NetworkEntity e: hypernetwork.getNetworkEntities()) {
			pe.getPossibility().put(e, mns.isPossible(e));
			int competitors = 0;
			int dependencies = 0;
			for(MinimalNetworkState m : mns.getMinimalNetworkStates(e)) {
				competitors += m.getImpossible().size();
				dependencies += m.getNecessary().size();
			}
			pe.getCompetitors().put(e, competitors);
			pe.getDependencies().put(e, dependencies);
		}
		
		effect = pe;
		
		mns.resetPerturbation();
	}
}
