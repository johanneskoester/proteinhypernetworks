/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcode;

import logicProteinHypernetwork.analysis.complexes.NetworkStateToSubnetwork;
import logicProteinHypernetwork.analysis.complexes.ProteinSubnetwork;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.SPINComplexRefinement;

/**
 *
 * @author koester
 */
public class MCODEComplexRefinement extends SPINComplexRefinement {

    public MCODEComplexRefinement(ProteinSubnetwork ppin, SPINComplexPrediction complexPrediction) {
        super(ppin, complexPrediction);
        networkStateToSubgraph = new NetworkStateToSubnetwork(ppin, true, true);
    }

    @Override
    protected SubcomplexCollector createSubcomplexCollector() {
        return new OmitFullyContainedSubcomplexCollector();
    }
}
