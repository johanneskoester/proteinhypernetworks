/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.visualization;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.PseudoHypergraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections15.Transformer;

/**
 * Fixed implementation of a hypergraph layout. Derived from the implementation
 * implementation at http://jung-framework-enhancements.origo.ethz.ch/.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class HypergraphLayout<V,E> implements Layout<V,E>,IterativeContext {
  private Layout<V,Integer> underlyingLayout;
  private PseudoHypergraph<V,E> pseudoHypergraph;

	/**
	 * Applies provided layout algorithm layoutclass to Hypergraph<V,E> hg and
	 * provides, besides positional data for the hypergraph vertices, a Graph<V,E>
	 * accessible by getGraph() which in turn provides access to the laid out
	 * hypergraph by getGraph().getHypergraph()
	 *
	 * @param hg Hypergraph that is to be laid out
	 * @param layoutclass Class object of a normal graph layout, which is to be applied
	 * to the hypergraph.
	 */
	@SuppressWarnings("unchecked") // suppress unavoidable warnings related to generics
	public HypergraphLayout(Hypergraph<V,E> hg, Class<? extends AbstractLayout> layoutclass) {

		// call constructor of superclass with pseudo-hypergraph as argument
		//super(new PseudoHypergraph<V, E>(hg));
    pseudoHypergraph = new PseudoHypergraph<V, E>(hg);

		// generate corresponding clique graph of hypergraph
		Graph<V,Integer> cliqueGraph = getCliqueGraphFromHypergraph(hg);

		try {
			// get constructor that takes a graph as only argument
			Constructor<? extends Layout> constructor = layoutclass.getConstructor(Graph.class);

			// instantiate the layout using this constructor
			this.underlyingLayout = constructor.newInstance(cliqueGraph);

		} catch (Exception e){
			System.out.println("Instantiation of AbstractLayout "+layoutclass.getName()+" failed. \n Using default layout FRLayout instead.");
			underlyingLayout = new FRLayout<V,Integer>(cliqueGraph);
		}
	}

  public Layout<V, Integer> getUnderlyingLayout() {
    return underlyingLayout;
  }

	/**
	 * Method to delegate vertex-to-point-transformation to the underlying layout.
	 */
	@Override
	public Point2D transform(V v) {
		Point2D result = underlyingLayout.transform(v);
		return result;
	}

	/**
	 * Constructs an auxiliary normal graph that reproduces the adjacencies
	 * in a hypergraph in terms of binary edges: each hyperedge of size k is represented
	 * by a subgraph on the same vertices isomorphic to a k-clique.
	 * As for each hyperedge of size k, O(k^2) edges are added, usage is
	 * not recommended for graphs with large hyperedges.
	 *
	 * @param hg A hypergraph that is to be laid out
	 * @return A graph g which can be laid out using a standard layout;
	 * V(g) = V(hg), E(hg) c E(g)
	 */
	public static <V,E> Graph<V,Integer> getCliqueGraphFromHypergraph(Hypergraph<V,E> hg){
		Graph<V,Integer> result = new SparseMultigraph<V,Integer>();
		for (V v: (Collection<V>) hg.getVertices()){
			result.addVertex(v);
		}

		for (E e:hg.getEdges()){
			ArrayList<V> a = new ArrayList<V>(hg.getIncidentVertices(e));
			for (int i = 0; i < a.size(); i++){
				for (int j = i+1; j < a.size(); j++){
					/*
					 * as java just truncates too large numbers,
					 * overflow doesn't have to be treated for our purposes
					 */
					Integer k = a.get(i).hashCode()*a.get(j).hashCode();
					while (result.containsEdge(k)){
						k++;
					}
					result.addEdge(k,a.get(i),a.get(j));
				}
			}
		}
		return result;
	}

	/**
	 * Initialize the layout.
	 */
	public void initialize() {
		this.underlyingLayout.initialize();
	}

	/**
	 * Set the size of the canvas for the layout.
	 */
	@Override
	public void setSize(Dimension size) {
		this.underlyingLayout.setSize(size);
	}

  @Override
  public Dimension getSize() {
    return this.underlyingLayout.getSize();
  }

	/**
	 * Reset the layout
	 */
	public void reset() {
		this.underlyingLayout.reset();
	}

  public void setInitializer(Transformer<V, Point2D> t) {
    this.underlyingLayout.setInitializer(t);
  }

  public void setGraph(Graph<V, E> graph) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Graph<V, E> getGraph() {
    return pseudoHypergraph;
  }

  public void lock(V v, boolean bln) {
    this.underlyingLayout.lock(v, bln);
  }

  public boolean isLocked(V v) {
    return this.underlyingLayout.isLocked(v);
  }

  public void setLocation(V v, Point2D pd) {
    this.underlyingLayout.setLocation(v, pd);
  }

  public void step() {
    ((IterativeContext)this.underlyingLayout).step();
  }

  public boolean done() {
    return ((IterativeContext)this.underlyingLayout).done();
  }
}
