/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * ConstraintEditorPanel.java
 *
 * Created on 14.03.2010, 12:17:58
 */
package proteinhypernetworkeditor.constraints;

import controller.Controller;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.interactions.Interaction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import modalLogic.formula.Literal;
import proteinHypernetwork.interactions.filters.FilterInteractionsByCommonProtein;
import proteinhypernetworkeditor.DumbMouseListener;
import util.TextLabel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstraintEditorPanel extends javax.swing.JPanel {

  private GridBagLayout gridBag;
  List<JCheckBox> notCheckBoxes = new Vector<JCheckBox>();
  List<JComboBox> interactionComboBoxes = new Vector<JComboBox>();
  List<JButton> removeButtons = new Vector<JButton>();
  List<TextLabel> ors = new Vector<TextLabel>();
  private ConstraintEditor constraintEditor;
  private int row;

  /** Creates new form ConstraintEditorPanel */
  public ConstraintEditorPanel(ConstraintEditor constraintEditor, int row) {
    initComponents();
    this.constraintEditor = constraintEditor;
    this.row = row;

    gridBag = (GridBagLayout) getLayout();
    notCheckBoxes.add(notCheckBox);
    interactionComboBoxes.add(networkEntityComboBox2);
    removeButtons.add(removeDisjunct);
    addMouseListener(new DumbMouseListener());

    constraintedNetworkEntity.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        /** @todo reactivate appropriately */
        //filterInteractions(); 
      }
    });
  }

  private void filterInteractions() {
    Interaction i = (Interaction) constraintedNetworkEntity.getSelectedItem();
    Collection items = new FilterInteractionsByCommonProtein().filter(Controller.getInstance().
            getHypernetwork().getInteractions(), i);

    for (JComboBox icb : interactionComboBoxes) {
      Interaction sel = (Interaction) icb.getSelectedItem();

      icb.removeAllItems();

      for (Object item : items) {
        icb.addItem(item);
        if (item.equals(sel)) {
          icb.setSelectedItem(item);
        }
      }
    }

    validate();
  }

  private void addLine() {
    JCheckBox newNotCheckBox = new JCheckBox(notCheckBox.getText());

    InteractionComboBox newInteractionComboBox = new InteractionComboBox();

    final JButton newRemoveButton = new JButton(removeDisjunct.getText());
    newRemoveButton.setPreferredSize(removeDisjunct.getPreferredSize());
    newRemoveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        removeLine(removeButtons.indexOf(newRemoveButton));
      }
    });

    add(newNotCheckBox);
    add(newInteractionComboBox);
    add(newRemoveButton);

    gridBag.setConstraints(newNotCheckBox, gridBag.getConstraints(notCheckBox));
    gridBag.setConstraints(newInteractionComboBox, gridBag.getConstraints(networkEntityComboBox2));
    gridBag.setConstraints(newRemoveButton, gridBag.getConstraints(removeDisjunct));

    notCheckBoxes.add(newNotCheckBox);
    interactionComboBoxes.add(newInteractionComboBox);
    removeButtons.add(newRemoveButton);

    GridBagConstraints orConstraints = gridBag.getConstraints(orButton);
    orConstraints.gridy = getLineCount() - 2;

    GridBagConstraints orButtonConstraints = gridBag.getConstraints(orButton);
    orButtonConstraints.gridy = getLineCount() - 1;
    gridBag.setConstraints(orButton, orButtonConstraints);

    TextLabel or = new TextLabel(java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/constraints/resources/ConstraintEditorPanel").getString("or"));
    add(or);
    ors.add(or);
    gridBag.setConstraints(or, orConstraints);

    constraintEditor.adjustTable(this, row);
  }

  private void removeLine(int line) {
    JCheckBox notCheckBox2 = notCheckBoxes.remove(line);
    JComboBox interactionComboBox = interactionComboBoxes.remove(line);
    JButton removeButton = removeButtons.remove(line);
    TextLabel or = ors.remove(line);

    remove(notCheckBox2);
    remove(interactionComboBox);
    remove(removeButton);
    remove(or);

    for (int i = line; i < ors.size(); i++) {
      TextLabel t = ors.get(i);
      GridBagConstraints c = gridBag.getConstraints(or);
      c.gridy = i;
      gridBag.setConstraints(t, c);
    }

    GridBagConstraints c = gridBag.getConstraints(orButton);
    c.gridy--;
    gridBag.setConstraints(orButton, c);


    validate();

    constraintEditor.adjustTable((JComponent) this.getParent(), row);
  }

  public void setConstrainingEntities(Collection<Literal<NetworkEntity>> ics) {

    for (int i = getLineCount(); i < ics.size(); i++) {
      addLine();
    }

    int line = 0;
    for (Literal<NetworkEntity> ic : ics) {
      notCheckBoxes.get(line).setSelected(ic.isNegation());
      interactionComboBoxes.get(line).setSelectedItem(ic.getProposition());
      line++;
    }
  }

  public Collection<NetworkEntity> getActivatingEntities() {
    List<NetworkEntity> ics = new ArrayList<NetworkEntity>();

    for (int i = 0; i < getLineCount(); i++) {
      NetworkEntity e = (NetworkEntity) interactionComboBoxes.get(i).getSelectedItem();
      if (!notCheckBoxes.get(i).isSelected()) {
        ics.add(e);
      }
    }

    return ics;
  }

  public Collection<NetworkEntity> getInhibitingEntities() {
    List<NetworkEntity> ics = new ArrayList<NetworkEntity>();

    for (int i = 0; i < getLineCount(); i++) {
      NetworkEntity e = (NetworkEntity) interactionComboBoxes.get(i).getSelectedItem();
      if (notCheckBoxes.get(i).isSelected()) {
        ics.add(e);
      }
    }

    return ics;
  }

  public NetworkEntity getConstrainedNetworkEntity() {
    return (NetworkEntity) constraintedNetworkEntity.getSelectedItem();
  }

  private void clear() {
    for (int i = 1; i < interactionComboBoxes.size(); i++) {
      removeLine(i);

    }
  }

  private int getLineCount() {
    return interactionComboBoxes.size();
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

        onlyIfLabel = new javax.swing.JLabel();
        notCheckBox = new javax.swing.JCheckBox();
        removeDisjunct = new javax.swing.JButton();
        orButton = new javax.swing.JButton();
        constraintedNetworkEntity = new proteinhypernetworkeditor.constraints.InteractionComboBox();
        networkEntityComboBox2 = new proteinhypernetworkeditor.constraints.InteractionComboBox();
        saveCancelPanel = new proteinhypernetworkeditor.SaveCancelPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(ConstraintEditorPanel.class);
        onlyIfLabel.setText(resourceMap.getString("onlyIfLabel.text")); // NOI18N
        onlyIfLabel.setName("onlyIfLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(onlyIfLabel, gridBagConstraints);

        notCheckBox.setText(resourceMap.getString("notCheckBox.text")); // NOI18N
        notCheckBox.setName("notCheckBox"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        add(notCheckBox, gridBagConstraints);

        removeDisjunct.setText(resourceMap.getString("removeDisjunct.text")); // NOI18N
        removeDisjunct.setEnabled(false);
        removeDisjunct.setName("removeDisjunct"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        add(removeDisjunct, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/constraints/resources/ConstraintEditorPanel"); // NOI18N
        orButton.setText(bundle.getString("or")); // NOI18N
        orButton.setName("orButton"); // NOI18N
        orButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        add(orButton, gridBagConstraints);

        constraintedNetworkEntity.setName("constraintedNetworkEntity"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(constraintedNetworkEntity, gridBagConstraints);

        networkEntityComboBox2.setName("networkEntityComboBox2"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        add(networkEntityComboBox2, gridBagConstraints);

        saveCancelPanel.setName("saveCancelPanel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(saveCancelPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void orButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orButtonActionPerformed
      addLine();
    }//GEN-LAST:event_orButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    proteinhypernetworkeditor.constraints.InteractionComboBox constraintedNetworkEntity;
    private proteinhypernetworkeditor.constraints.InteractionComboBox networkEntityComboBox2;
    private javax.swing.JCheckBox notCheckBox;
    private javax.swing.JLabel onlyIfLabel;
    private javax.swing.JButton orButton;
    private javax.swing.JButton removeDisjunct;
    proteinhypernetworkeditor.SaveCancelPanel saveCancelPanel;
    // End of variables declaration//GEN-END:variables
}
