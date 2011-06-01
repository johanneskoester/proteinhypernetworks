/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor;

import javax.swing.table.TableModel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public interface ManagedTableModel extends TableModel {
  public void addRow();
  public void removeRow(int index);
}
