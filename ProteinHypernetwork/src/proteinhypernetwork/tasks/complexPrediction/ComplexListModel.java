/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetwork.tasks.complexPrediction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;
import javax.swing.AbstractListModel;
import logicProteinHypernetwork.analysis.complexes.Complex;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexListModel extends AbstractListModel {

  private Vector<Complex> showing;
  private Vector<Complex> complexes;

  public void setComplexes(Collection<Complex> cs) {
    complexes = new Vector<Complex>(cs);
    Collections.sort(complexes);

    showing = complexes;
    super.fireContentsChanged(this, 0, complexes.size() - 1);
  }

  @Override
  public int getSize() {
    if (showing != null) {
      return showing.size();
    }
    return 0;
  }

  @Override
  public Object getElementAt(int index) {
    return showing.elementAt(index);
  }

  public boolean isFiltered() {
    return showing != complexes;
  }

  public void filter(String s) {
    boolean invert = false;
    if (s.startsWith("!")) {
      s = s.substring(1);
      invert = true;
    }
    int oldSize;
    if (s.isEmpty()) {
      oldSize = showing.size();
      showing = complexes;
    } else {
      oldSize = showing.size();
      showing = new Vector<Complex>();
      for (Complex c : complexes) {
        if (!invert == c.toString().contains(s)) {
          showing.add(c);
        }
      }
    }
    fireContentsChanged(this, 0, oldSize - 1);
  }

  public void clear() {
    setComplexes(new ArrayList<Complex>());
  }
}
