/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OptionsDialog.java
 *
 * Created on Apr 6, 2011, 1:45:11 PM
 */

package proteinhypernetwork;

import logicProteinHypernetwork.analysis.complexes.lcma.LCMAComplexPrediction;
import logicProteinHypernetwork.analysis.complexes.offline.OfflineComplexPrediction;

/**
 *
 * @author koester
 */
public class OptionsDialog extends javax.swing.JDialog {

    /** Creates new form OptionsDialog */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
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

    buttonGroup1 = new javax.swing.ButtonGroup();
    tabs = new javax.swing.JTabbedPane();
    complexPrediction = new javax.swing.JPanel();
    predictionMethodLabel = new javax.swing.JLabel();
    complexPredictionCommand = new javax.swing.JTextField();
    lcma = new javax.swing.JRadioButton();
    command = new javax.swing.JRadioButton();
    closeButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setName("Form"); // NOI18N

    tabs.setName("tabs"); // NOI18N

    complexPrediction.setName("complexPrediction"); // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(OptionsDialog.class);
    predictionMethodLabel.setText(resourceMap.getString("predictionMethodLabel.text")); // NOI18N
    predictionMethodLabel.setName("predictionMethodLabel"); // NOI18N

    complexPredictionCommand.setText(resourceMap.getString("complexPredictionCommand.text")); // NOI18N
    complexPredictionCommand.setToolTipText(resourceMap.getString("complexPredictionCommand.toolTipText")); // NOI18N
    complexPredictionCommand.setName("complexPredictionCommand"); // NOI18N

    buttonGroup1.add(lcma);
    lcma.setSelected(true);
    lcma.setText(resourceMap.getString("lcma.text")); // NOI18N
    lcma.setName("lcma"); // NOI18N
    lcma.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        lcmaActionPerformed(evt);
      }
    });

    buttonGroup1.add(command);
    command.setText(resourceMap.getString("command.text")); // NOI18N
    command.setName("command"); // NOI18N
    command.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        commandActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout complexPredictionLayout = new javax.swing.GroupLayout(complexPrediction);
    complexPrediction.setLayout(complexPredictionLayout);
    complexPredictionLayout.setHorizontalGroup(
      complexPredictionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(complexPredictionLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(complexPredictionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(predictionMethodLabel)
          .addGroup(complexPredictionLayout.createSequentialGroup()
            .addComponent(command)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(complexPredictionCommand, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
          .addComponent(lcma))
        .addContainerGap())
    );
    complexPredictionLayout.setVerticalGroup(
      complexPredictionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(complexPredictionLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(predictionMethodLabel)
        .addGap(6, 6, 6)
        .addComponent(lcma)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(complexPredictionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(complexPredictionCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(command))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabs.addTab(resourceMap.getString("complexPrediction.TabConstraints.tabTitle"), complexPrediction); // NOI18N

    closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
    closeButton.setName("closeButton"); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
          .addComponent(closeButton))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(closeButton)
        .addContainerGap())
    );

    tabs.getAccessibleContext().setAccessibleName(resourceMap.getString("tabs.AccessibleContext.accessibleName")); // NOI18N

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void lcmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lcmaActionPerformed
      ProteinHypernetworkApp.getApplication().setComplexPrediction(LCMAComplexPrediction.class);
    }//GEN-LAST:event_lcmaActionPerformed

    private void commandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandActionPerformed
      ProteinHypernetworkApp.getApplication().setComplexPrediction(OfflineComplexPrediction.class);
    }//GEN-LAST:event_commandActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.JButton closeButton;
  private javax.swing.JRadioButton command;
  private javax.swing.JPanel complexPrediction;
  private javax.swing.JTextField complexPredictionCommand;
  private javax.swing.JRadioButton lcma;
  private javax.swing.JLabel predictionMethodLabel;
  private javax.swing.JTabbedPane tabs;
  // End of variables declaration//GEN-END:variables

}
