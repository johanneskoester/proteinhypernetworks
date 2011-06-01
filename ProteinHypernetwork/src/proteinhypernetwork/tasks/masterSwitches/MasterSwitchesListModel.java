/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetwork.tasks.masterSwitches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import logicProteinHypernetwork.analysis.pis.PIS;
import logicProteinHypernetwork.analysis.pis.SinglePIS;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MasterSwitchesListModel extends AbstractListModel {

  private List<PIS> showing;
  private List<PIS> masterSwitches;

  public void setMasterSwitches(List<? extends PIS> masterSwitches) {
    this.masterSwitches = new ArrayList<PIS>(masterSwitches);
    showing = this.masterSwitches;
    Collections.sort(this.masterSwitches, new Comparator<PIS>() {

      @Override
      public int compare(PIS o1, PIS o2) {
        return -o1.compareTo(o2);
      }
    });
    fireContentsChanged(this, 0, masterSwitches.size()-1);
  }

  @Override
  public int getSize() {
    if(showing == null) {
      return 0;
    }
    return showing.size();
  }

  @Override
  public Object getElementAt(int index) {
    return showing.get(index);
  }

  public boolean isFiltered() {
    return showing != masterSwitches;
  }

  public void filter(String e) {
    boolean invert = false;
    if (e.startsWith("!")) {
      e = e.substring(1);
      invert = true;
    }
    int oldSize;
    if(e.isEmpty()) {
      oldSize = showing.size();
      showing = masterSwitches;
    }
    else {
      oldSize = showing.size();
      showing = new Vector<PIS>();
      for(PIS s : masterSwitches) {
        if(!invert == s.toString().contains(e)) {
          showing.add(s);
        }
      }
    }

    fireContentsChanged(this, 0, oldSize-1);
  }
}
