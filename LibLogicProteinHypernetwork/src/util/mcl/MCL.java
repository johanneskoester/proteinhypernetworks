/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcl;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.knowceans.mcl.MarkovClustering;
import org.knowceans.mcl.SparseMatrix;

/**
 *
 * @author koester
 */
public class MCL<V, E, C extends Collection<V>>  implements Transformer<UndirectedGraph<V, E>, Collection<C>> {

  private MarkovClustering mcl = new MarkovClustering();
  private Factory<C> complexFactory;

  public MCL(Factory<C> complexFactory) {
    this.complexFactory = complexFactory;
  }

  public Collection<C> transform(UndirectedGraph<V, E> g) {
    GraphMatrix m = new GraphMatrix(g);
    SparseMatrix clusters = mcl.run(m, 1E-4, 2.1, 0, 1E-15);
    
    Set<C> complexes = new HashSet<C>();
    for(int i = 0; i < g.getVertexCount(); i++) {
      complexes.add(getComplex(i, clusters, m));
    }
    
    return complexes;
  }
  
  private C getComplex(int i, SparseMatrix clusters, GraphMatrix<V,E> m) {
    boolean[] visited = new boolean[m.getVertexCount()];
    
    Deque<Integer> todo = new ArrayDeque<Integer>();
    
    visited[i] = true;
    todo.add(i);
    
    while(!todo.isEmpty()) {
      int u = todo.removeFirst();
      
      for(int v = 0; v < m.getVertexCount(); v++) {
        if(clusters.get(u, v) > 0 && !visited[v]) {
          visited[v] = true;
          todo.addLast(v);
        }
      }
    }
    
    C complex = complexFactory.create();
    
    for(int v = 0; v < m.getVertexCount(); v++) {
      if(visited[v])
        complex.add(m.intToVertex(v));
    }
    return complex;
  }
}
