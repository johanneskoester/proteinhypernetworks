/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logicProteinHypernetwork.analysis.complexes.mcode;

import java.util.ArrayList;
import java.util.Collection;
import logicProteinHypernetwork.analysis.complexes.Complex;
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
        networkStateToSubgraph = new MCODENetworkStateToSubnetwork(ppin);
    }

  @Override
  public Collection<Complex> transform(Complex complex) {
    Collection<Complex> complexes = new ArrayList<Complex>();
    if(complex.size() > 30)
      return complexes;
    for(Complex c : super.transform(complex)) {
      if(c.size() <= 30)
        complexes.add(c);
    }
    return complexes;
  }
    
    

    @Override
    protected SubcomplexCollector createSubcomplexCollector() {
        return new OmitFullyContainedSubcomplexCollector();
    }
}
