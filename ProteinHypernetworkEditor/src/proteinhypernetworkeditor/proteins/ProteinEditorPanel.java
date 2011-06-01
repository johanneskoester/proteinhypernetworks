/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * ProteinEditorPanel.java
 *
 * Created on 26.07.2010, 09:51:54
 */

package proteinhypernetworkeditor.proteins;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinEditorPanel extends javax.swing.JPanel {

    /** Creates new form ProteinEditorPanel */
    public ProteinEditorPanel() {
        initComponents();
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

    protein = new javax.swing.JTextField();
    saveCancelPanel = new proteinhypernetworkeditor.SaveCancelPanel();

    setName("Form"); // NOI18N
    setLayout(new java.awt.GridBagLayout());

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(ProteinEditorPanel.class);
    protein.setText(resourceMap.getString("protein.text")); // NOI18N
    protein.setMaximumSize(new java.awt.Dimension(200, 200));
    protein.setName("protein"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(protein, gridBagConstraints);

    saveCancelPanel.setName("saveCancelPanel"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    add(saveCancelPanel, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  javax.swing.JTextField protein;
  proteinhypernetworkeditor.SaveCancelPanel saveCancelPanel;
  // End of variables declaration//GEN-END:variables

}
