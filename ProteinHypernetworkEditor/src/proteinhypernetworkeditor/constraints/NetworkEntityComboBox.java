/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proteinhypernetworkeditor.constraints;

import controller.Controller;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author koester
 */
public class NetworkEntityComboBox extends JComboBox {

  public NetworkEntityComboBox() {
    super(new Vector(Controller.getInstance().getHypernetwork().getNetworkEntities()));
  }

}
