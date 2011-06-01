/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * SaveCancelPanel.java
 *
 * Created on 26.07.2010, 09:35:17
 */

package proteinhypernetworkeditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class SaveCancelPanel extends javax.swing.JPanel {

    /** Creates new form SaveCancelPanel */
    public SaveCancelPanel() {
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

    saveButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();

    setName("Form"); // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(SaveCancelPanel.class);
    saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
    saveButton.setName("saveButton"); // NOI18N

    cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
    cancelButton.setName("cancelButton"); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(saveButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cancelButton))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(saveButton)
        .addComponent(cancelButton))
    );
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JButton cancelButton;
  public javax.swing.JButton saveButton;
  // End of variables declaration//GEN-END:variables

}