/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetworkVisualization.implementation.jung.visualization;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.JComponent;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class DefaultGraphStyle {

  private static Transformer<Object, Shape> vertexShapeTransformer = new Transformer<Object, Shape>() {

    private VertexShapeFactory vsf = new VertexShapeFactory(new Transformer<Object, Integer>() {

      public Integer transform(Object i) {
        return 15;
      }
    }, new Transformer<Object, Float>() {

      public Float transform(Object i) {
        return 1.0f;
      }
    });

    public Shape transform(Object v) {
      return vsf.getEllipse(v);
    }
  };

  public static Transformer<Object, Shape> getVertexShapeTransformer() {
    return vertexShapeTransformer;
  }

  public static void setGraphStyle(final VisualizationViewer vv, boolean labelVertices) {
    vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
    vv.getRenderContext().setVertexShapeTransformer(getVertexShapeTransformer());
    vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Object, Paint>() {

      public Paint transform(Object v) {
        Color c = Color.RED;//vv.getBackground().darker();
        if (vv.getPickedVertexState().isPicked(v)) {
          c = Color.WHITE;//c.brighter();
        }
        return makeTransparent(c);
      }
    });
    vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<Object, Paint>() {

      public Paint transform(Object v) {
        if (vv.getPickedVertexState().isPicked(v)) {
          return makeTransparent(Color.WHITE);
        }
        return makeTransparent(Color.RED);
      }
    });
    vv.getRenderContext().setVertexStrokeTransformer(new Transformer<Object, Stroke>() {

      public Stroke transform(Object i) {
        return new BasicStroke(0);
      }
    });
    vv.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<Object, Paint>() {
      Graph g = vv.getGraphLayout().getGraph();

      public Paint transform(Object e) {
        for(Object v : g.getEndpoints(e)) {
          if(vv.getPickedVertexState().isPicked(v))
            return makeTransparent(Color.RED);
        }
        return makeTransparent(vv.getBackground().darker());//vv.getBackground().darker();
      }
    });
    vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<Object, Stroke>() {
      Graph g = vv.getGraphLayout().getGraph();

      public Stroke transform(Object e) {
        for(Object v : g.getEndpoints(e)) {
          if(vv.getPickedVertexState().isPicked(v))
            return new BasicStroke(3);
        }
        return new BasicStroke(1);
      }
    });
    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
    labelVertices(vv, false);

    //vv.setVertexToolTipTransformer(new ToStringLabeller());
  }

  public static void labelVertices(final VisualizationViewer vv, boolean label) {
    if (!label) {
      vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.GRAY.darker()) {

        @Override
        public <V> Component getVertexLabelRendererComponent(JComponent co, Object value, Font font, boolean isSelected, V vertex) {
          if (!isSelected) {
            value = null;
          }
          return super.getVertexLabelRendererComponent(co, value, font, isSelected, vertex);
        }
      });
    } else {
      vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.GRAY.darker()));
    }
  }

  public static Color makeTransparent(Color c) {
    return new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
  }
}
