/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor.proteins;

import java.awt.event.ActionEvent;
import proteinHypernetwork.proteins.Protein;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import proteinhypernetworkeditor.ManagedTableCellEditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinEditor extends ManagedTableCellEditor {

  private ProteinEditorPanel editorPanel;
  private Protein editing;

  public ProteinEditor() {
  }

  @Override
  public Object getCellEditorValue() {
    editing.setId(editorPanel.protein.getText());
    return editing;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if(isSelected) {
      editorPanel = new ProteinEditorPanel();
      editing = (Protein)value;
      editorPanel.protein.setText(editing.getId());

      editorPanel.saveCancelPanel.saveButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(e.getID() == ActionEvent.ACTION_PERFORMED) {
            ProteinEditor.this.reallyStopCellEditing();
          }
        }
      });
      editorPanel.saveCancelPanel.cancelButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(e.getID() == ActionEvent.ACTION_PERFORMED) {
            ProteinEditor.this.cancelCellEditing();
          }
        }
      });

      setupComponent(editorPanel, row);

      SwingUtilities.invokeLater(new Runnable() {
        @Override
                public void run() {
                        // This needs to be in an invokeLater() to work properly
                        editorPanel.requestFocusInWindow();
                }
        });

      return editorPanel;
    }
    return null;
  }
}
