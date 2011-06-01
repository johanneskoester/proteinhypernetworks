/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetworkeditor.interactions;

import java.awt.event.ActionEvent;
import proteinHypernetwork.interactions.Interaction;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;
import proteinhypernetworkeditor.ManagedTableCellEditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class InteractionEditor extends ManagedTableCellEditor {

  private InteractionEditorPanel editorPanel;
  private Interaction editing;

  public InteractionEditor() {
  }

  @Override
  public Object getCellEditorValue() {

    Iterator<Interactor> interactors = editing.iterator();
    Iterator<ProteinComboBox> proteins = editorPanel.proteinComboBoxes();
    Iterator<ProteinDomainEditor> domains = editorPanel.proteinDomainEditors();
    Interactor[] is = {interactors.next(), interactors.next()};
    int i = 0;
    while(proteins.hasNext()) {
      boolean add = false;
      if(is[i] == null) {
        is[i] = new Interactor();
        add = true;
      }

      is[i].setProtein((Protein)proteins.next().getSelectedItem());
      ProteinDomainEditor d = domains.next();
      if(d.getDomainDefined()) {
        is[i].setDomain(d.getDomain());
      }
      else {
        is[i].setDomain(null);
      }
      if(add)
        editing.addInteractor(is[i]);
      i++;
    }

    return editing;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (isSelected) {
      editing = (Interaction) value;
      table.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
      
      editorPanel = new InteractionEditorPanel();
      editorPanel.saveCancelPanel.saveButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(e.getID() == ActionEvent.ACTION_PERFORMED) {
            InteractionEditor.this.reallyStopCellEditing();
          }
        }
      });
      editorPanel.saveCancelPanel.cancelButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(e.getID() == ActionEvent.ACTION_PERFORMED) {
            InteractionEditor.this.cancelCellEditing();
          }
        }
      });

      editorPanel.setValues(editing);

      /*if(editing.getReference() != null) {
        editorPanel.referenceEditor.setExperiments(editing.getReference().getExperiments());
        editorPanel.referenceEditor.setLiterature(editing.getReference().getLiterature());
      }*/

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
