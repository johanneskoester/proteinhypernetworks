/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.NetworkStateToSubnetwork;
import logicProteinHypernetwork.analysis.complexes.ProteinSubnetwork;
import logicProteinHypernetwork.analysis.complexes.SPINComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.SPINComplexRefinement;
import proteinHypernetwork.proteins.Protein;

/**
 *
 * @author koester
 */
public class MCODEComplexRefinement extends SPINComplexRefinement {

    public MCODEComplexRefinement(ProteinSubnetwork ppin, SPINComplexPrediction complexPrediction) {
        super(ppin, complexPrediction);
        networkStateToSubgraph = new NetworkStateToSubnetwork(ppin, false, false);
    }

  @Override
  public Collection<Complex> transform(Complex complex) {
    Collection<Complex> complexes = new ArrayList<Complex>();
    if(complex.size() > 30)
      return complexes;
    
    /*Set<Protein> proteins = new HashSet<Protein>(complex);
    for(Complex c : super.transform(complex)) {
      if(proteins.containsAll(c))
        complexes.add(c);
    }*/
    return super.transform(complex);
  }
    
    

    @Override
    protected SubcomplexCollector createSubcomplexCollector() {
        return new OmitFullyContainedSubcomplexCollector();
    }
}
