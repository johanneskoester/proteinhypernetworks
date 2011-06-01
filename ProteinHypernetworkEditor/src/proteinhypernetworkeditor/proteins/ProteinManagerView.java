/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor.proteins;

import controller.Controller;
import javax.swing.event.TableModelEvent;
import proteinHypernetwork.proteins.Protein;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.event.TableModelListener;
import proteinhypernetworkeditor.ManagerView;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinManagerView extends ManagerView {

  public ProteinManagerView() {
    super(Controller.getInstance().getProteinTable(), new ProteinEditor());

    /*getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent e) {
        getVisualizationPanel().getVisualizationViewer().
            getPickedVertexState().clear();
        for(int r : getTable().getSelectedRows()) {
          Object o = getValueAt(r);
          getVisualizationPanel().getVisualizationViewer().
            getPickedVertexState().pick(o, true);
        }
      }
    });*/

    getTable().getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        setInfoText("Proteins: " + getTable().getRowCount());
      }
    });
  }

  @Override
  protected void filterAction(final String text) {
    RowFilter rf = new RowFilter() {

      @Override
      public boolean include(Entry entry) {
        Protein p = (Protein)entry.getValue(0);

        if(text.startsWith("!")) {
          return !p.toString().contains(text.substring(1));
        }

        return p.toString().contains(text);
      }
    };

    filterRows(rf);
  }

  @Override
  protected void addAction() {
    Controller.getInstance().addProtein();
  }

  @Override
  protected void removeAction(int row) {
    Controller.getInstance().removeProtein(row);
  }

  @Override
  protected void removeAction(int[] rows) {
    Controller.getInstance().removeProteins(rows);
  }
}
