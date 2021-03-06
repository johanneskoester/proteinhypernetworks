/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * ReferenceEditor.java
 *
 * Created on 14.03.2010, 11:06:42
 */

package proteinhypernetworkeditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ReferenceEditor extends javax.swing.JPanel {

    /** Creates new form ReferenceEditor */
    public ReferenceEditor() {
        initComponents();
    }

    public String getExperiments() {
      return experimentsField.getText();
    }

    public String getLiterature() {
      return literatureField.getText();
    }

    public void setExperiments(String text) {
      experimentsField.setText(text);
    }

    public void setLiterature(String text) {
      literatureField.setText(text);
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

    experimentsLabel = new javax.swing.JLabel();
    experimentsField = new javax.swing.JTextField();
    literatureLabel = new javax.swing.JLabel();
    literatureField = new javax.swing.JTextField();

    setName("Form"); // NOI18N
    setLayout(new java.awt.GridBagLayout());

    experimentsLabel.setLabelFor(experimentsField);
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(ReferenceEditor.class);
    experimentsLabel.setText(resourceMap.getString("experimentsLabel.text")); // NOI18N
    experimentsLabel.setName("experimentsLabel"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(experimentsLabel, gridBagConstraints);

    experimentsField.setText(resourceMap.getString("experimentsField.text")); // NOI18N
    experimentsField.setName("experimentsField"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(experimentsField, gridBagConstraints);

    literatureLabel.setLabelFor(literatureField);
    literatureLabel.setText(resourceMap.getString("literatureLabel.text")); // NOI18N
    literatureLabel.setName("literatureLabel"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    add(literatureLabel, gridBagConstraints);

    literatureField.setText(resourceMap.getString("literatureField.text")); // NOI18N
    literatureField.setName("literatureField"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(literatureField, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField experimentsField;
  private javax.swing.JLabel experimentsLabel;
  private javax.swing.JTextField literatureField;
  private javax.swing.JLabel literatureLabel;
  // End of variables declaration//GEN-END:variables

}
