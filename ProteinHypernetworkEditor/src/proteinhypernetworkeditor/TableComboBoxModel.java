/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class TableComboBoxModel extends AbstractListModel implements ComboBoxModel,TableModelListener {
  
  private DefaultTableModel table;
  private Object selected;
  private Set<ListDataListener> listeners = new HashSet<ListDataListener>();

  public TableComboBoxModel(DefaultTableModel proteins) {
    this.table = proteins;
  }

  @Override
  public void setSelectedItem(Object anItem) {
    selected = anItem;
  }

  @Override
  public Object getSelectedItem() {
    return selected;
  }

  @Override
  public int getSize() {
    return table.getRowCount();
  }

  @Override
  public Object getElementAt(int index) {
    Object o = ((Vector)table.getDataVector().elementAt(index)).firstElement();
    return ((Vector)table.getDataVector().elementAt(index)).firstElement();
  }

  @Override
  public void addListDataListener(ListDataListener l) {
    listeners.add(l);
  }

  @Override
  public void removeListDataListener(ListDataListener l) {
    listeners.remove(l);
  }

  private void selectFirst() {
    setSelectedItem(getElementAt(0));
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    if(e.getType() == TableModelEvent.DELETE) {
      for(Object r : table.getDataVector()) {
        if(((Vector)r).contains(selected)) {
          return;
        }
      }
      selectFirst(); // selected element was deleted.
      fireIntervalRemoved(this, e.getFirstRow(), e.getLastRow());
    }
    else if(e.getType() == TableModelEvent.INSERT) {
      fireIntervalAdded(this, e.getFirstRow(), e.getLastRow());
    }
    else if(e.getType() == TableModelEvent.UPDATE) {
      fireContentsChanged(this, e.getFirstRow(), e.getLastRow());
    }
  }
}
