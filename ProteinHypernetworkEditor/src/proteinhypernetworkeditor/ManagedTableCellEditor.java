/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor;

import java.awt.event.ContainerListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class ManagedTableCellEditor extends AbstractCellEditor implements TableCellEditor {
  private JTable table;
  private ManagerView managerView;

  private ContainerListener containerListener;
  
  public void setManagerView(ManagerView managerView) {
    this.table = managerView.getTable();
    this.managerView = managerView;
  }

  protected void fireEditingCanceled() {
    super.fireEditingCanceled();
    resetRowHeight();
  }

  @Override
  public void fireEditingStopped() {
    super.fireEditingStopped();
    resetRowHeight();
  }

  @Override
  public boolean stopCellEditing() {
    return false;
  }



  public boolean reallyStopCellEditing() {
    fireEditingStopped();
    return true;
  }

  protected void resetRowHeight() {
    table.setRowHeight(table.getRowHeight());
  }

  protected void setupComponent(final JComponent comp, final int row) {
    adjustTable(comp, row);
  }

  public void adjustTable(JComponent comp, int row) {
    table.setRowHeight(row, comp.getPreferredSize().height);
    table.scrollRectToVisible(table.getCellRect(row, 0, true));
  }
}
