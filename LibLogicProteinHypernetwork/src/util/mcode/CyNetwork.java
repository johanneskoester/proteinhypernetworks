/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcode;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author koester
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class CyNetwork<V, E> implements GraphPerspective {

    private UndirectedGraph<V, E> graph;
    private List<Node<V>> nodes;
    private List<Node<V>> subgraphnodes;

    public CyNetwork(UndirectedGraph<V, E> graph) {
        this.graph = graph;
        nodes = new ArrayList<Node<V>>();
        int i = 0;
        for (V v : graph.getVertices()) {
            nodes.add(new Node<V>(v, getNodeIndex(i++)));
        }
        subgraphnodes = nodes;
    }

    public CyNetwork(UndirectedGraph<V, E> graph, CyNetwork<V, E> rootGraph) {
        this.graph = graph;
        nodes = rootGraph.nodes;
        subgraphnodes = new ArrayList<Node<V>>(graph.getVertexCount());
        for(Node<V> n : nodes) {
            if(graph.containsVertex(n.getVertex()))
                subgraphnodes.add(n);
        }
    }

    public int getNodeCount() {
        return graph.getVertexCount();
    }

    public Iterator nodesIterator() {
        return subgraphnodes.iterator();
    }

    public GraphPerspective createGraphPerspective(int[] nodeIndices) {
        UndirectedGraph<V, E> subgraph = new UndirectedSparseGraph<V, E>();

        for (int i : nodeIndices) {
            subgraph.addVertex(nodes.get(getInternalIndex(i)).getVertex());
        }

        for (E e : graph.getEdges()) {
            Pair<V> ep = graph.getEndpoints(e);
            if (subgraph.containsVertex(ep.getFirst()) && subgraph.containsVertex(ep.getSecond())) {
                subgraph.addEdge(e, ep);
            }
        }

        return new CyNetwork(subgraph, this);
    }

    public int[] neighborsArray(int nodeIndex) {
        Set<V> nb = new HashSet<V>(graph.getNeighbors(nodes.get(getInternalIndex(nodeIndex)).getVertex()));
        int[] neighbors = new int[nb.size()];

        int j = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (nb.contains(nodes.get(i).getVertex())) {
                neighbors[j++] = getNodeIndex(i);
            }
        }
        return neighbors;
    }

    public int[] getNodeIndicesArray() {
        int[] indices = new int[graph.getVertexCount()];
        int j = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (graph.containsVertex(nodes.get(i).getVertex())) {
                indices[j++] = getNodeIndex(i);
            }
        }
        return indices;
    }

    public int getRootGraphNodeIndex(int nodeIndex) {
        if (graph.containsVertex(nodes.get(getInternalIndex(nodeIndex)).getVertex())) {
            return nodeIndex;
        }
        return 0;
    }

    public boolean isNeighbor(Node n, Node m) {
        return graph.isNeighbor((V) n.getVertex(), (V) m.getVertex());
    }

    public int getEdgeCount() {
        return graph.getEdgeCount();
    }

    public int getDegree(Node n) {
        V v = (V) n.getVertex();
        if (graph.containsVertex(v)) {
            return graph.degree(v);
        }
        return -1;
    }

    private static int getNodeIndex(int i) {
        return i + 1;
    }

    private static int getInternalIndex(int i) {
        return i - 1;
    }

    public UndirectedGraph getGraph() {
        return graph;
    }

    private class NodesIterator implements Iterator<Node<V>> {

        private Node<V> next;
        private Iterator<Node<V>> ite = nodes.iterator();

        public NodesIterator() {
            movetoNext();
        }

        private void movetoNext() {
            next = null;
            while (ite.hasNext() && (next == null || !graph.containsVertex(next.getVertex()))) {
                next = ite.next();
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node<V> next() {
            Node<V> n = next;
            movetoNext();
            return n;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }
}
