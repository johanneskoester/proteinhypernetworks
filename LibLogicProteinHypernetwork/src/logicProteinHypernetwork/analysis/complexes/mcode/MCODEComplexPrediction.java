/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcode;

import java.util.Collection;
import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.ComplexFactory;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import util.mcode.MCODE;

/**
 *
 * @author koester
 */
public class MCODEComplexPrediction extends SPINComplexPrediction {

    public MCODEComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
        super(hypernetwork, perturbations, threadCount);
        this.plainComplexPrediction = new MCODE<Protein, Interaction, Complex>(new ComplexFactory());
    }

    @Override
    public void refinement(Collection<Complex> naiveComplexes) {
        super.refinement(naiveComplexes, new MCODEComplexRefinement(ppin, this));
        //complexes = naiveComplexes;
    }
}
