/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * ComplexPrediction.java
 *
 * Created on 25.03.2010, 17:53:31
 */
package proteinhypernetwork.tasks.complexPrediction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import proteinhypernetwork.Controller;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexPredictionView extends javax.swing.JPanel {

  private Controller controller = Controller.getInstance();

  /** Creates new form ComplexPrediction */
  public ComplexPredictionView() {
    initComponents();

    Controller.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Controller.COMPLEXESPREDICTED))
          updateComplexes();
      }
    });
  }

  public void clear() {
    complexListModel.clear();
  }

  private void updateComplexes() {
    complexListModel.setComplexes(Controller.getInstance().getComplexes());
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    complexListModel = new proteinhypernetwork.tasks.complexPrediction.ComplexListModel();
    jScrollPane1 = new javax.swing.JScrollPane();
    complexList = new javax.swing.JList();
    filter = new javax.swing.JTextField();

    setName("Form"); // NOI18N

    jScrollPane1.setName("jScrollPane1"); // NOI18N

    complexList.setModel(complexListModel);
    complexList.setName("complexList"); // NOI18N
    jScrollPane1.setViewportView(complexList);

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(ComplexPredictionView.class);
    filter.setText(resourceMap.getString("filter.text")); // NOI18N
    filter.setToolTipText(resourceMap.getString("filter.toolTipText")); // NOI18N
    filter.setName("filter"); // NOI18N
    filter.setPreferredSize(new java.awt.Dimension(200, 28));
    filter.putClientProperty("JTextField.variant", "search");
    filter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        filterActionPerformed(evt);
      }
    });
    filter.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        filterFocusGained(evt);
      }
      public void focusLost(java.awt.event.FocusEvent evt) {
        filterFocusLost(evt);
      }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .add(filter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
      .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(filter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterActionPerformed
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(ComplexPredictionView.class);
    if(!filter.getText().equals(resourceMap.getString("filter.text")))
      complexListModel.filter(filter.getText());
  }//GEN-LAST:event_filterActionPerformed

  private void filterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filterFocusGained
    if(filter.getText().equals(org.jdesktop.application.Application.getInstance(
            proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(
            ComplexPredictionView.class).getString("filter.text")))
      filter.setText("");
  }//GEN-LAST:event_filterFocusGained

  private void filterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filterFocusLost
    if(!complexListModel.isFiltered())
      filter.setText(org.jdesktop.application.Application.getInstance(
            proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(
            ComplexPredictionView.class).getString("filter.text"));
  }//GEN-LAST:event_filterFocusLost

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JList complexList;
  private proteinhypernetwork.tasks.complexPrediction.ComplexListModel complexListModel;
  private javax.swing.JTextField filter;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables
}
