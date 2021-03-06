/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

/*
 * ManagedTableCellRenderer2.java
 *
 * Created on 26.07.2010, 10:50:36
 */

package proteinhypernetworkeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ManagedTableCellRenderer2 extends javax.swing.JPanel implements TableCellRenderer {

    /** Creates new form ManagedTableCellRenderer2 */
    public ManagedTableCellRenderer2() {
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

        cellContent = new javax.swing.JLabel();
        editButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(ManagedTableCellRenderer2.class);
        cellContent.setText(resourceMap.getString("cellContent.text")); // NOI18N
        cellContent.setName("cellContent"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(cellContent, gridBagConstraints);

        editButton.setText(resourceMap.getString("editButton.text")); // NOI18N
        editButton.setName("editButton"); // NOI18N
        editButton.setVisible(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(editButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cellContent;
    private javax.swing.JButton editButton;
    // End of variables declaration//GEN-END:variables

    private ActionListener clickButton;

  @Override
  public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
    setValue(value);
    if(isSelected) {
      setBackground(table.getSelectionBackground());
      editButton.setVisible(true);
      editButton.removeActionListener(clickButton);

      clickButton = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    table.editCellAt(row, column);
                }
            };

      editButton.addActionListener(clickButton);
    }
    else {
      setBackground(table.getBackground());
      editButton.setVisible(false);
      editButton.removeActionListener(clickButton);
    }
    
    return this;
  }

  @Override
  public Dimension getPreferredSize() {
      return editButton.getPreferredSize();
  }

  protected void setValue(Object value) {
    cellContent.setText(value.toString());
  }

  @Override
  public boolean isOpaque() {
    return true;
  }

  @Override
  public void revalidate() {
    
  }

  @Override
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    
  }
  
  @Override
  public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {

  }

  @Override
  public void repaint() {
  }


}
