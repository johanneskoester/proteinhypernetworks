/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetwork.tasks.complexPrediction;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexTableCellRenderer extends DefaultTableCellRenderer {
  private int row = 0;

  @Override
  protected void setValue(Object value) {
    super.setValue(row + ": " + value);
  }



  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    this.row = row;
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }

  
}
