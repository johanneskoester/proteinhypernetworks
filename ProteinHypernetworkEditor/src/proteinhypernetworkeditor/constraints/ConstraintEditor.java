/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetworkeditor.constraints;

import java.awt.event.ActionEvent;
import proteinHypernetwork.constraints.Constraint;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import modalLogic.formula.Formula;
import modalLogic.formula.Literal;
import modalLogic.formula.factory.FormulaFactory;
import proteinHypernetwork.NetworkEntity;
import proteinhypernetworkeditor.ManagedTableCellEditor;
import proteinhypernetworkeditor.ReferenceEditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstraintEditor extends ManagedTableCellEditor {

  private ConstraintEditorPanel constraintEditor;
  private ReferenceEditor referenceEditor;
  private JPanel panel;
  private Constraint editing;

  public ConstraintEditor() {
  }

  @Override
  public Object getCellEditorValue() {

    FormulaFactory<NetworkEntity> formula = new FormulaFactory<NetworkEntity>();
    formula.openImplication();
    formula.literal(constraintEditor.getConstrainedNetworkEntity());

    Collection<NetworkEntity> activators = constraintEditor.getActivatingEntities();
    Collection<NetworkEntity> inhibitors = constraintEditor.getInhibitingEntities();

    boolean disj = activators.size() + inhibitors.size() > 1;

    if (disj) {
      formula.openDisjunction();
    }
    for (NetworkEntity e : activators) {
      formula.literal(e);
    }
    for (NetworkEntity e : inhibitors) {
      formula.negation();
      formula.literal(e);
    }
    if (disj) {
      formula.close();
    }

    editing.setImplication(formula.create());

    return editing;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, final int row, int column) {

    if (isSelected) {
      editing = (Constraint) value;

      constraintEditor = new ConstraintEditorPanel(this, row);

      constraintEditor.saveCancelPanel.saveButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if (e.getID() == ActionEvent.ACTION_PERFORMED) {
            ConstraintEditor.this.reallyStopCellEditing();
          }
        }
      });
      constraintEditor.saveCancelPanel.cancelButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if (e.getID() == ActionEvent.ACTION_PERFORMED) {
            ConstraintEditor.this.cancelCellEditing();
          }
        }
      });

      if (editing.getImplication() != null) {
        constraintEditor.constraintedNetworkEntity.setSelectedItem(editing.getImplication().getChild(0).getProposition());
      }

      if (editing.getImplication() != null) {
        Collection<Literal<NetworkEntity>> entities = new ArrayList<Literal<NetworkEntity>>();
        Formula<NetworkEntity> f = editing.getImplication().getChild(1);
        if (f.getType() == Formula.LITERAL) {
          entities.add((Literal<NetworkEntity>) f);
        } else {
          for (Formula<NetworkEntity> c : f) {
            entities.add((Literal<NetworkEntity>) c);
          }
        }
        constraintEditor.setConstrainingEntities(entities);
      }

      setupComponent(constraintEditor, row);

      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          // This needs to be in an invokeLater() to work properly
          constraintEditor.requestFocusInWindow();
        }
      });

      return constraintEditor;
    }

    return null;
  }
}
