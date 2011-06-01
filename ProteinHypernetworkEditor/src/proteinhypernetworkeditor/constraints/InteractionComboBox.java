/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetworkeditor.constraints;

import controller.Controller;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class InteractionComboBox extends JComboBox {

  public InteractionComboBox() {
    super(new Vector(Controller.getInstance().getHypernetwork().getNetworkEntities()));
  }
}
