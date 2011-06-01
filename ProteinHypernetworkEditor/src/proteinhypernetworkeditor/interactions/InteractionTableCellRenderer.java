/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor.interactions;

import proteinHypernetwork.interactions.Interaction;
import proteinhypernetworkeditor.ManagedTableCellRenderer2;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class InteractionTableCellRenderer extends ManagedTableCellRenderer2 {

  @Override
  protected void setValue(Object value) {
    Interaction i = (Interaction)value;
    //Reference ref = i.getReference();

    /*String desc = "<html>" + i;

    if(ref != null)
      desc += "<br/>" + ref;
    desc += "</html>";*/

    super.setValue(i);
  }
}
