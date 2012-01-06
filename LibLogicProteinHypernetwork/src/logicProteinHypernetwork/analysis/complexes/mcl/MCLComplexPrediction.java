/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcl;

import logicProteinHypernetwork.Perturbations;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.ComplexFactory;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import util.mcl.MCL;

/**
 *
 * @author koester
 */
public class MCLComplexPrediction extends SPINComplexPrediction {

  public MCLComplexPrediction(ProteinHypernetwork hypernetwork, Perturbations perturbations, int threadCount) {
    super(hypernetwork, perturbations, threadCount);
    this.plainComplexPrediction = new MCL<Protein, Interaction, Complex>(new ComplexFactory());
  }
  
}
