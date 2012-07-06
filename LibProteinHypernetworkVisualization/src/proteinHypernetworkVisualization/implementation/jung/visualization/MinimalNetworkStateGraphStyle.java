/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinHypernetworkVisualization.implementation.jung.visualization;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import proteinHypernetworkVisualization.implementation.jung.graphs.JungMinimalNetworkStatesGraph.EntityType;
import proteinHypernetworkVisualization.implementation.jung.graphs.JungMinimalNetworkStatesGraph.TypedEntity;

/**
 *
 * @author koester
 */
public class MinimalNetworkStateGraphStyle extends DefaultGraphStyle {
  
  @Override
  protected Color getDefaultVertexColor(Object vertex, final VisualizationViewer vv) {
    TypedEntity e = (TypedEntity)vertex;
    if (e.getType() == EntityType.IMPOSSIBLE)
      return Color.GREEN;
    return Color.RED;
  }
  
  @Override
  protected Color getDefaultEdgeColor(Object edge, final VisualizationViewer vv) {
    TypedEntity e = (TypedEntity)edge;
    if (e.getType() == EntityType.IMPOSSIBLE)
      return Color.GREEN;
    return Color.RED;
  }
}
