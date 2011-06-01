/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.proteins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.filters.FilterInteractionsByProtein;

/**
 * A list of proteins.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Proteins extends ArrayList<Protein> {

  private ProteinHypernetwork hypernetwork;
  private Map<String, Protein> proteinIndex;

  /**
   * Constructor of class Proteins.
   *
   * @param hypernetwork the protein hypernetwork
   */
  public Proteins(ProteinHypernetwork hypernetwork) {
    this.hypernetwork = hypernetwork;
  }

  /**
   * Pre calculates an index of protein ids and proteins for O(1) search of proteins.
   * buildIndex() itself needs O(|P|) once. So you have to decide about the tradeoff.
   * buildIndex() has to be repeated if protein set P changes.
   */
  public void buildIndex() {
    proteinIndex = new HashMap<String, Protein>();
    for (Protein p : this) {
      proteinIndex.put(p.getId(), p);
    }
  }

  /**
   * Returns true if index was build.
   *
   * @return true if index was build
   */
  public boolean hasIndex() {
    return proteinIndex != null;
  }

  /**
   * Checks whether protein with a certain id is contained.
   *
   * @param id the id
   * @return whether the protein is contained
   */
  public boolean containsProteinWithId(String id) {
    return getProteinById(id) != null;
  }

  /**
   * Returns the protein object with a given id. Warning, this method runs in 
   * O(|P|) if buildIndex() is not performed before. Else it runs in O(1) but
   * buildIndex() itself needs O(|P|) once. So you have to decide about the tradeoff.
   * buildIndex() hase to be repeated if protein set P changes.
   *
   * @param id the protein id
   * @return the protein
   */
  public Protein getProteinById(String id) {
    if (proteinIndex == null) {
      for (Protein p : this) {
        if (p.getId().equals(id)) {
          return p;
        }
      }
      return null;
    } else {
      return proteinIndex.get(id);
    }
  }

  @Override
  @SuppressWarnings("element-type-mismatch")
  public boolean remove(Object o) {

    if (o instanceof Protein) {
      removeInteractions((Protein) o);
    }

    return super.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    for (Object o : c) {
      if (o instanceof Protein) {
        removeInteractions((Protein) o);
      }
    }
    return super.removeAll(c);
  }

  /**
   * Removes all interactions with a given protein.
   *
   * @param p the protein
   */
  private void removeInteractions(Protein p) {
    Collection<Interaction> is = new FilterInteractionsByProtein().filter(hypernetwork.getInteractions(), p);
    hypernetwork.getInteractions().removeAll(is);
  }
}
