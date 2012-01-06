/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcl;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowceans.mcl.SparseMatrix;

/**
 *
 * @author koester
 */
public class GraphMatrix<V,E> extends SparseMatrix {
  
  private Map<V, Integer> vertexToInt = new HashMap<V, Integer>();
  private List<V> intToVertex = new ArrayList<V>();
  private UndirectedGraph<V,E> graph;

  public GraphMatrix(UndirectedGraph<V, E> graph) {
    super(graph.getVertexCount(), graph.getVertexCount());
    this.graph = graph;
    
    int i = 0;
    for(V v : graph.getVertices()) {
      intToVertex.add(v);
      vertexToInt.put(v, i);
      i++;
    }
    
    for(E e : graph.getEdges()) {
      Pair<V> ep = graph.getEndpoints(e);
      this.add(vertexToInt.get(ep.getFirst()), vertexToInt.get(ep.getSecond()), 1);
    }
  }
  
  public int vertexToInt(V v) {
    return vertexToInt.get(v);
  }
  
  public V intToVertex(int i) {
    return intToVertex.get(i);
  }
  
  public int getVertexCount() {
    return graph.getVertexCount();
  }
}
