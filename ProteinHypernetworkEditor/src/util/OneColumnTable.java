/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class OneColumnTable extends AbstractTableModel {

  private Vector table;

  public OneColumnTable(Collection items) {
    table = new Vector(items);
  }

  public void setData(Collection items) {
    table = new Vector(items);
  }

  @Override
  public int getRowCount() {
    return table.size();
  }

  @Override
  public int getColumnCount() {
    return 1;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return "";
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return Object.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return table.get(rowIndex);
  }

  @Override
  public void setValueAt(Object o, int rowIndex, int columnIndex) {
    table.set(rowIndex, o);
  }

  public void addRow(Object o) {
    table.add(o);
    fireTableRowsInserted(table.size()-1, table.size()-1);
  }

  public void removeRow(int index) {
    table.remove(index);
    fireTableRowsDeleted(index, index);
  }

  public int getRow(Object o) {
    return table.indexOf(o);
  }

  public void fireTableChanged() {
    super.fireTableDataChanged();
  }
}
