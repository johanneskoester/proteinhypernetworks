/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * Tasks.java
 *
 * Created on 25.03.2010, 17:50:32
 */
package proteinhypernetwork;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import logicProteinHypernetwork.analysis.complexes.Complex;
import proteinHypernetworkVisualization.implementation.jung.visualization.JungMasterSwitchesVisualization;
import proteinHypernetworkVisualization.implementation.jung.visualization.JungProteinComplexVisualization;
import proteinHypernetworkVisualization.implementation.jung.visualization.JungVisualization;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class TasksView extends javax.swing.JPanel {

  private JPanel current;
  private GridLayout layout;
  private String TASK = "task";
  public static String VISUALIZATIONACTIVE = "visualizationActive";
  private JungProteinComplexVisualization complexesVisualization;
  private JungMasterSwitchesVisualization masterSwitchVisualization;

  /** Creates new form Tasks */
  public TasksView() {
    initComponents();

    Controller.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Controller.COMPLEXESPREDICTED)) {
          complexPrediction.setEnabled(true);
        }
        if (evt.getPropertyName().equals(Controller.MASTERSWITCHESPREDICTED)) {
          masterSwitchesView.setEnabled(true);
        }
        if (evt.getPropertyName().equals(Controller.FUNCTIONALSIMILARITIESPREDICTED)) {
          functionalSimilaritiesView.setEnabled(true);
        }
        if (evt.getPropertyName().equals(Controller.LOADED)) {
          complexPrediction.setEnabled(false);
          masterSwitchesView.setEnabled(false);
          perturbationView.setEnabled(true);
        }
      }
    });



    layout = (GridLayout) getLayout();

    add(complexPrediction);
    current = complexPrediction;

    Controller.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Controller.LOADED)) {
          complexesVisualization = null;
          masterSwitchVisualization = null;
          complexPrediction.clear();
        }
        if (evt.getPropertyName().equals(Controller.COMPLEXESPREDICTED)) {
          complexesVisualization = null;
        }
        if (evt.getPropertyName().equals(Controller.MASTERSWITCHESPREDICTED)) {
          masterSwitchVisualization = null;
        }
      }
    });
  }

  public JungVisualization getCurrentVisualization() {
    if(current instanceof JungMasterSwitchesVisualization ||
            current instanceof JungProteinComplexVisualization) {
      return (JungVisualization)current;
    }
    throw new UnsupportedOperationException("No visualization active");
  }

  public void activateComplexPredictionView() {
    activateView(complexPrediction);
    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.FALSE);
  }

  public void activateMasterSwitchesView() {
    activateView(masterSwitchesView);
    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.FALSE);
  }

  public void activateFunctionalSimilaritiesView() {
    activateView(functionalSimilaritiesView);
    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.FALSE);
  }

  public void activatePerturbationView() {
    activateView(perturbationView);
    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.FALSE);
  }

  public void activateComplexVisualization() {
    Dimension visSize = new Dimension(getSize().width - 100, getSize().height - 100);
    if (complexesVisualization == null) {
      complexesVisualization = new JungProteinComplexVisualization();
      complexesVisualization.setVisualizationSize(visSize);
      complexesVisualization.setProteinComplexes(Controller.getInstance().
              getLogicHypernetwork().getProteinHypernetwork(),
              new ArrayList<Complex>((Controller.getInstance().getComplexes())));
    } else {
      complexesVisualization.setVisualizationSize(visSize);
    }
    activateView(complexesVisualization);

    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.TRUE);
  }

  public void activateMasterSwitchVisualization() {
    Dimension visSize = new Dimension(getSize().width - 100, getSize().height - 100);
    if (masterSwitchVisualization == null) {
      masterSwitchVisualization = new JungMasterSwitchesVisualization();
      masterSwitchVisualization.setVisualizationSize(visSize);
      masterSwitchVisualization.setProteinHypernetwork(Controller.getInstance().getLogicHypernetwork());
    } else {
      masterSwitchVisualization.setVisualizationSize(visSize);
    }
    activateView(masterSwitchVisualization);
    firePropertyChange(VISUALIZATIONACTIVE, null, Boolean.TRUE);
  }

  private void activateView(JPanel view) {
    if (current != view) {
      remove(current);
      current = view;
      add(current);
      updateUI();
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

    complexPrediction = new proteinhypernetwork.tasks.complexPrediction.ComplexPredictionView();
    masterSwitchesView = new proteinhypernetwork.tasks.masterSwitches.MasterSwitchesView();
    perturbationView = new proteinhypernetwork.tasks.perturbation.PerturbationView();
    functionalSimilaritiesView = new proteinhypernetwork.tasks.functionalSimilarities.FunctionalSimilaritiesView();

    complexPrediction.setName("complexPrediction"); // NOI18N

    masterSwitchesView.setName("masterSwitchesView"); // NOI18N

    perturbationView.setName("perturbationView"); // NOI18N

    functionalSimilaritiesView.setName("functionalSimilaritiesView"); // NOI18N

    setName("Form"); // NOI18N
    setOpaque(false);
    setLayout(new java.awt.GridLayout(1, 1));
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private proteinhypernetwork.tasks.complexPrediction.ComplexPredictionView complexPrediction;
  private proteinhypernetwork.tasks.functionalSimilarities.FunctionalSimilaritiesView functionalSimilaritiesView;
  private proteinhypernetwork.tasks.masterSwitches.MasterSwitchesView masterSwitchesView;
  private proteinhypernetwork.tasks.perturbation.PerturbationView perturbationView;
  // End of variables declaration//GEN-END:variables
}
