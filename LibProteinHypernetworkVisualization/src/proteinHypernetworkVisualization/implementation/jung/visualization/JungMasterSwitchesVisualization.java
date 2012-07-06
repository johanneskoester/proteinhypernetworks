/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * JungMasterSwitchesVisualization.java
 *
 * Created on 27.07.2010, 20:29:20
 */
package proteinHypernetworkVisualization.implementation.jung.visualization;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.pis.PIS;
import logicProteinHypernetwork.analysis.pis.SinglePIS;
import org.apache.commons.collections15.Transformer;
import org.apache.xmlrpc.XmlRpcException;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetworkVisualization.implementation.jung.graphs.JungProteinNetworkGraph;
import proteinHypernetworkVisualization.visualization.CytoscapeVisualization;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class JungMasterSwitchesVisualization extends javax.swing.JPanel implements JungVisualization {

  private LogicProteinHypernetwork hypernetwork;
  private DefaultModalGraphMouse<SinglePIS, SinglePIS> graphMouse =
          new DefaultModalGraphMouse<SinglePIS, SinglePIS>();
  private VisualizationViewer currentVisualization;
  private Dimension size = new Dimension(300, 300);
  private Graph currentGraph;

  /** Creates new form JungMasterSwitchesVisualization */
  public JungMasterSwitchesVisualization() {
    initComponents();
  }

  public void saveToPNG(File file) throws IOException {
    BufferedImage bi = new BufferedImage(currentVisualization.getSize().width, currentVisualization.getSize().height, BufferedImage.TYPE_INT_RGB);

    Color bg = currentVisualization.getBackground();
    currentVisualization.setBackground(Color.WHITE);
    currentVisualization.setDoubleBuffered(false);

    currentVisualization.paintAll(bi.createGraphics());

    ImageIO.write(bi, "png", file);
    currentVisualization.setDoubleBuffered(true);
    currentVisualization.setBackground(bg);
  }

  public void saveToGraphML(File file) throws IOException {
    GraphMLWriter w = new GraphMLWriter();
    w.setVertexIDs(new ToStringLabeller());
    w.save(currentGraph, new FileWriter(file));
  }

  public void setVisualizationSize(Dimension d) {
    size = d;
  }

  public Graph getCurrentGraph() {
    return currentGraph;
  }

  public void setProteinHypernetwork(LogicProteinHypernetwork hypernetwork) {
    this.hypernetwork = hypernetwork;

    UndirectedGraph g = new UndirectedSparseGraph();
    currentGraph = g;

    boolean proteins = hypernetwork.getSinglePIS().get(0).getEntity() instanceof Protein;
    if (proteins) {
      Map<Protein, SinglePIS> mmap = new HashMap<Protein, SinglePIS>();
      for (SinglePIS m : hypernetwork.getSinglePIS()) {
        g.addVertex(m);
        mmap.put((Protein) m.getEntity(), m);
      }
      for (Interaction i : hypernetwork.getProteinHypernetwork().getInteractions()) {
        g.addEdge(i, mmap.get(i.getProteins().getFirst()), mmap.get(i.getProteins().getSecond()));
      }

    } else {
      for (Protein p : hypernetwork.getProteinHypernetwork().getProteins()) {
        g.addVertex(p);
      }
      for (SinglePIS m : hypernetwork.getSinglePIS()) {
        Interaction i = ((Interaction) m.getEntity());
        g.addEdge(m, i.getProteins());
      }
    }

    Layout l = new BalloonHubLayout(g);
    //((FRLayout) l).setMaxIterations(10);
    l.setSize(size);

    vv = new VisualizationViewer(l, size);
    currentVisualization = vv;
    vv.setDoubleBuffered(true);
    new DefaultGraphStyle().setGraphStyle(vv, true);
    vv.setGraphMouse(graphMouse);

    vv.getRenderContext().setVertexShapeTransformer(new MasterSwitchShapeTransformer());

    vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<Object, Stroke>() {

      public Stroke transform(Object o) {
        if (o instanceof SinglePIS) {
          return new BasicStroke(((PIS) o).getScore() / 10 + 1);
        }
        return new BasicStroke(1);
      }
    });

    GraphZoomScrollPane graphZoomScrollPane = new GraphZoomScrollPane(vv);
    graph.add(graphZoomScrollPane);
  }

  private class MasterSwitchShapeTransformer implements Transformer<Object, Shape> {

    private EllipseVertexShapeTransformer vst = new EllipseVertexShapeTransformer();

    public MasterSwitchShapeTransformer() {
      vst.setSizeTransformer(new Transformer<Object, Integer>() {

        public Integer transform(Object o) {
          if (o instanceof SinglePIS) {
            return new Integer(Math.round(((PIS) o).getScore() / 10)) + 5;
          }
          return 5;
        }
      });
    }

    @Override
    public Shape transform(Object v) {
      return vst.transform(v);
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    mouseModes = new javax.swing.ButtonGroup();
    graph = new javax.swing.JPanel();
    controls = new javax.swing.JPanel();
    labelVertices = new javax.swing.JToggleButton();
    moveZoomButton = new javax.swing.JToggleButton();
    selectButton = new javax.swing.JToggleButton();
    showCytoscape = new javax.swing.JButton();

    setLayout(new java.awt.GridBagLayout());

    graph.setLayout(new java.awt.GridLayout(1, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(graph, gridBagConstraints);

    controls.setLayout(new java.awt.GridBagLayout());

    labelVertices.setText("Label Vertices");
    labelVertices.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        labelVerticesActionPerformed(evt);
      }
    });
    controls.add(labelVertices, new java.awt.GridBagConstraints());

    mouseModes.add(moveZoomButton);
    moveZoomButton.setText("Move and Zoom");
    moveZoomButton.setSelected(true);
    moveZoomButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        moveZoomButtonActionPerformed(evt);
      }
    });
    controls.add(moveZoomButton, new java.awt.GridBagConstraints());

    mouseModes.add(selectButton);
    selectButton.setText("Drag Vertices");
    selectButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectButtonActionPerformed(evt);
      }
    });
    controls.add(selectButton, new java.awt.GridBagConstraints());

    showCytoscape.setText("Show in Cytoscape");
    showCytoscape.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        showCytoscapeActionPerformed(evt);
      }
    });
    controls.add(showCytoscape, new java.awt.GridBagConstraints());

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    add(controls, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void moveZoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveZoomButtonActionPerformed
    if (moveZoomButton.isSelected()) {
      graphMouse.setMode(Mode.TRANSFORMING);
    }
  }//GEN-LAST:event_moveZoomButtonActionPerformed

  private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
    if (selectButton.isSelected()) {
      graphMouse.setMode(Mode.PICKING);
    }
  }//GEN-LAST:event_selectButtonActionPerformed

  private void labelVerticesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelVerticesActionPerformed
    new DefaultGraphStyle().labelVertices(vv, labelVertices.isSelected());
  }//GEN-LAST:event_labelVerticesActionPerformed

  private void showCytoscapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCytoscapeActionPerformed
    JungProteinNetworkGraph g = new JungProteinNetworkGraph();
    g.setProteinNetwork(hypernetwork.getProteinHypernetwork());

    Map<String, Object> pis = new HashMap<String, Object>();
    for (SinglePIS ms : hypernetwork.getSinglePIS()) {
      pis.put(ms.getEntity().toString(), new Integer((int) ms.getScore()));
    }

    try {

      CytoscapeVisualization v = new CytoscapeVisualization(g);
      v.visualize();

      v.addVertexAttributes("pis", "Integer", pis);
    } catch (XmlRpcException ex) {
      String message = ex.getMessage();
      message += "Is Cytoscape with CytoscapeRPC plugin running?";
      JOptionPane.showMessageDialog(null, message);
    }
  }//GEN-LAST:event_showCytoscapeActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel controls;
  private javax.swing.JPanel graph;
  private javax.swing.JToggleButton labelVertices;
  private javax.swing.ButtonGroup mouseModes;
  private javax.swing.JToggleButton moveZoomButton;
  private javax.swing.JToggleButton selectButton;
  private javax.swing.JButton showCytoscape;
  // End of variables declaration//GEN-END:variables
  private VisualizationViewer vv;
}
