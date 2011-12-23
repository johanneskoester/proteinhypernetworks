/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.mcode;

/**
 *
 * @author koester
 */
public class Node<V> {
    private V vertex;
    private int rootGraphIndex;

    public Node(V vertex, int rootGraphIndex) {
        this.vertex = vertex;
        this.rootGraphIndex = rootGraphIndex;
    }
    
    public V getVertex() {
        return vertex;
    }
    
    public int getRootGraphIndex() {
        return rootGraphIndex;
    }
}
