/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor.constraints;

import controller.Controller;
import proteinHypernetwork.constraints.Constraint;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import proteinHypernetwork.constraints.filters.FilterConstraintsByString;
import proteinhypernetworkeditor.ManagerView;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ConstraintManagerView extends ManagerView {

  public ConstraintManagerView() {
    super(Controller.getInstance().getConstraintTable(), new ConstraintEditor());
    getAddButton().setEnabled(false);

    Controller.getInstance().
            getInteractionTable().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        getAddButton().setEnabled(Controller.getInstance().
                getInteractionTable().getRowCount() +
                Controller.getInstance().
                getProteinTable().getRowCount() > 1);
      }
    });

    getTable().getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        setInfoText("Constraints: " + getTable().getRowCount());
      }
    });
  }

  @Override
  protected void filterAction(final String text) {
    RowFilter rf = new RowFilter() {

      @Override
      public boolean include(Entry entry) {
        Constraint c = (Constraint)entry.getValue(0);

        if(text.startsWith("!")) {
          return !c.toString().contains(text.substring(1));
        }

        return c.toString().contains(text);
      }
    };

    filterRows(rf);
  }

  @Override
  protected void addAction() {
    Controller.getInstance().addConstraint();
    editLastRow();
  }

  @Override
  protected void removeAction(int row) {
    Controller.getInstance().removeConstraint(row);
  }

  @Override
  protected void removeAction(int[] rows) {
    Controller.getInstance().removeConstraints(rows);
  }
}
