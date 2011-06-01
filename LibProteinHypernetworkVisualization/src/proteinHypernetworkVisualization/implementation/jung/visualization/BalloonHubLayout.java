/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetworkVisualization.implementation.jung.visualization;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Hierarchical circular layout preferring hub proteins as circle centers.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class BalloonHubLayout<V, E> implements Layout<V, E>, org.apache.commons.collections15.Transformer<V, Point2D> {

  private Graph<V, E> graph;
  private Forest<V, Object> hubForest;
  private Layout<V, Object> balloonLayout;

  public BalloonHubLayout(Graph<V, E> graph) {
    setGraph(graph);
    balloonLayout = new BalloonLayout<V, Object>(hubForest);
  }

  public void initialize() {
    balloonLayout.initialize();
  }

  public void setInitializer(Transformer<V, Point2D> t) {
    balloonLayout.setInitializer(t);
  }

  public void setGraph(final Graph<V, E> graph) {
    List<V> vertices = new ArrayList<V>(graph.getVertices());
    Collections.sort(vertices, new Comparator<V>() {

      public int compare(V v1, V v2) {
        return graph.degree(v2) - graph.degree(v1);
      }
    });

    hubForest = new DelegateForest<V, Object>();

    for (V u : vertices) {
      hubForest.addVertex(u);
      for (V v : graph.getSuccessors(u)) {
        if (!hubForest.containsVertex(v)) {
          hubForest.addEdge(new Object(), u, v);
        }
      }
    }

    this.graph = graph;
  }

  public Graph<V, E> getGraph() {
    return graph;
  }

  public void reset() {
    balloonLayout.reset();
  }

  public void setSize(Dimension dmnsn) {
    balloonLayout.setSize(dmnsn);
  }

  public Dimension getSize() {
    return balloonLayout.getSize();
  }

  public void lock(V v, boolean bln) {
    balloonLayout.lock(v, bln);
  }

  public boolean isLocked(V v) {
    return balloonLayout.isLocked(v);
  }

  public void setLocation(V v, Point2D pd) {
    balloonLayout.setLocation(v, pd);
  }

  public Point2D transform(V i) {
    return balloonLayout.transform(i);
  }
}
