/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetworkeditor.interactions;

import controller.Controller;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ProteinComboBox extends JComboBox {

  public ProteinComboBox() {
    super(new Vector(Controller.getInstance().getHypernetwork().getProteins()));
  }
}
