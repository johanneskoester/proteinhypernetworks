/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * MasterSwitches.java
 *
 * Created on 29.03.2010, 15:45:15
 */
package proteinhypernetwork.tasks.functionalSimilarities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import proteinhypernetwork.Controller;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FunctionalSimilaritiesView extends javax.swing.JPanel {
  

  /** Creates new form MasterSwitches */
  public FunctionalSimilaritiesView() {
    initComponents();

    Controller.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Controller.FUNCTIONALSIMILARITIESPREDICTED)){
          //functionalSimilaritiesListModel.setFunctionalSimilarities(Controller.getInstance().getFunctionalSimilarities());
        }
      }
    });
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    functionalSimilaritiesListModel = new proteinhypernetwork.tasks.functionalSimilarities.FunctionalSimilaritiesListModel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList1 = new javax.swing.JList();
    filter = new javax.swing.JTextField();

    setName("Form"); // NOI18N

    jScrollPane1.setName("jScrollPane1"); // NOI18N

    jList1.setModel(functionalSimilaritiesListModel);
    jList1.setName("jList1"); // NOI18N
    jScrollPane1.setViewportView(jList1);

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(FunctionalSimilaritiesView.class);
    filter.setText(resourceMap.getString("filter.text")); // NOI18N
    filter.setName("filter"); // NOI18N
    filter.setPreferredSize(new java.awt.Dimension(200, 28));
    filter.putClientProperty("JTextField.variant", "search");
    filter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        filterActionPerformed(evt);
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
        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(filter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterActionPerformed
    functionalSimilaritiesListModel.filter(filter.getText());
  }//GEN-LAST:event_filterActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField filter;
  private proteinhypernetwork.tasks.functionalSimilarities.FunctionalSimilaritiesListModel functionalSimilaritiesListModel;
  private javax.swing.JList jList1;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables
}
