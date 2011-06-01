/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinhypernetwork.tasks.functionalSimilarities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarity;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FunctionalSimilaritiesListModel extends AbstractListModel {

  private List<FunctionalSimilarity> showing;
  private List<FunctionalSimilarity> functionalSimilarities;

  public void setFunctionalSimilarities(List<FunctionalSimilarity> functionalSimilarities) {
    this.functionalSimilarities = new ArrayList<FunctionalSimilarity>(functionalSimilarities);
    showing = this.functionalSimilarities;
    Collections.sort(this.functionalSimilarities, new Comparator<FunctionalSimilarity>() {

      @Override
      public int compare(FunctionalSimilarity o1, FunctionalSimilarity o2) {
        return -o1.compareTo(o2);
      }
    });
    fireContentsChanged(this, 0, functionalSimilarities.size()-1);
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

  public void filter(String e) {
    int oldSize;
    if(e.isEmpty()) {
      oldSize = showing.size();
      showing = functionalSimilarities;
    }
    else {
      oldSize = showing.size();
      showing = new Vector<FunctionalSimilarity>();
      for(FunctionalSimilarity s : functionalSimilarities) {
        if(s.toString().contains(e)) {
          showing.add(s);
        }
      }
    }

    fireContentsChanged(this, 0, oldSize-1);
  }
}
