/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetworkeditor.interactions;

import controller.Controller;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.interactions.filters.FilterInteractionsByString;
import proteinhypernetworkeditor.ManagerView;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class InteractionManagerView extends ManagerView {

  private boolean picking = false;
  private boolean selecting = false;

  public InteractionManagerView() {
    super(Controller.getInstance().getInteractionTable(),
            new InteractionEditor(), new InteractionTableCellRenderer());
    getAddButton().setEnabled(false);

    Controller.getInstance().
            getProteinTable().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        getAddButton().setEnabled(Controller.getInstance().
                getProteinTable().getRowCount() > 0);

      }
    });

    /*Controller.getInstance().
            getInteractionTable().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          OneColumnTable table = Controller.getInstance().
                  getInteractionTable();
          Interaction changed = (Interaction)table.getValueAt(e.getFirstRow(), 0);

          Collection<Interaction> filtered = new InteractionBindingsiteFilter(
                  Controller.getInstance().getHypernetwork().getInteractions()).filter(changed);

          for (Interaction i : filtered) {
            if (i != changed) {

              Constraint c = new Competition(i, changed).getConstraint();

              if (!new ConstraintFilter(Controller.getInstance().getHypernetwork().getConstraints()).contains(c)) {
                if (JOptionPane.showConfirmDialog(InteractionManagerView.this,
                        java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/interactions/resources/InteractionManagerView").getString("confirm_competition"))
                        == JOptionPane.OK_OPTION) {
                  Controller.getInstance().getConstraintTable().addConstraint(c);
                }
              }
            }
          }
        }
      }
    });*/

    /*getVisualizationPanel().getVisualizationViewer().
            getPickedEdgeState().addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        for (Object o : getVisualizationPanel().getVisualizationViewer().getPickedEdgeState().getPicked()) {
          selectRow(o);
        }
      }
    });*/

    /*getVisualizationPanel().getVisualizationViewer().
            getPickedVertexState().addItemListener(new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        Set<Protein> proteins = getVisualizationPanel().getVisualizationViewer().getPickedVertexState().getPicked();

        if (proteins.size() == 1) {
          selectRows(getTableModel().getInteractions(proteins.iterator().next()));
        } else {
          getTable().clearSelection();
        }

      }
    });*/

    getTable().getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        setInfoText("Interactions: " + getTable().getRowCount());
      }
    });
  }

  @Override
  protected void addAction() {
    /*Set<Protein> proteins = getVisualizationPanel().getVisualizationViewer().getPickedVertexState().getPicked();
    Set<Interaction> interactions = getVisualizationPanel().getVisualizationViewer().getPickedEdgeState().getPicked();

    Interaction i;
    if (proteins.size() == 2) {
      i = new Interaction();
      for(Protein p : proteins) {
        i.addInteractor(new Interactor(p));
      }
    }*/
    /*for (Interaction i : interactions) {
      Interaction j = new Interaction();
      j.setProtein1(i.getProtein1());
      j.setProtein2(i.getProtein2());

      getTableModel().addInteraction(j);
      editLastRow();

      notUseful = false;
    }*/

    /*else {
      i = new Interaction();
    }*/

    Controller.getInstance().addInteraction();
    editLastRow();
  }

  @Override
  protected void removeAction(int row) {
    Controller.getInstance().removeInteraction(row);
  }

  @Override
  protected void removeAction(int[] rows) {
    Controller.getInstance().removeInteractions(rows);
  }

  @Override
  protected void filterAction(final String text) {
    RowFilter rf = new RowFilter() {

      @Override
      public boolean include(Entry entry) {
        Interaction i = (Interaction) entry.getValue(0);

        if(text.startsWith("!")) {
          return !i.toString().contains(text.substring(1));
        }

        return i.toString().contains(text);
      }
    };

    filterRows(rf);
  }
}
