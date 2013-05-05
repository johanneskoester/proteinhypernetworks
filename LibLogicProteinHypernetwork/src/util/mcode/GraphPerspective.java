/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcode;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.util.Iterator;

/**
 *
 * @author koester
 */
@SuppressWarnings( { "rawtypes" } )
public interface GraphPerspective<V,E> {
    public int getNodeCount();
    public int[] getNodeIndicesArray();
    public int getRootGraphNodeIndex(int nodeIndex);
    public Iterator nodesIterator();
    public boolean isNeighbor(Node n, Node m);
    public int getEdgeCount();
    public int getDegree(Node n);
    public GraphPerspective createGraphPerspective(int[] nodeIndices);
    public UndirectedGraph<V,E> getGraph();
}
