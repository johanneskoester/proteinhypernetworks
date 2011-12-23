/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcode;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author koester
 */
public class MCODE<V, E, C extends Collection<V>>  implements Transformer<UndirectedGraph<V, E>, Collection<C>> {
    
    private Factory<C> complexFactory;

    public MCODE(Factory<C> complexFactory) {
        this.complexFactory = complexFactory;
    }

    public Collection<C> transform(UndirectedGraph<V, E> g) {
        CyNetwork<V, E> network = new CyNetwork<V, E>(g);
        
        MCODEAlgorithm mcode = new MCODEAlgorithm(new TaskMonitor(), null);
        
        mcode.scoreGraph(network, "scores");
        MCODECluster[] clusters = mcode.findClusters(network, "clusters");
        
        Collection<C> complexes = new ArrayList<C>();
        for(MCODECluster c : clusters) {
            C complex = complexFactory.create();
            complex.addAll(((UndirectedGraph<V,E>)c.getGPCluster().getGraph()).getVertices());
            complexes.add(complex);
        }
        
        return complexes;
    }
    
}
