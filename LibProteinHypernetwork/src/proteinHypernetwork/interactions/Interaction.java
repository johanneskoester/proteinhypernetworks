/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.interactions;

import edu.uci.ics.jung.graph.util.Pair;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.collections15.iterators.ArrayIterator;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.AbstractListenable;
import proteinHypernetwork.util.Listener;

/**
 * An interaction between two proteins / protein domains.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Interaction extends AbstractListenable implements Iterable<Interactor>, NetworkEntity, Comparable<Interaction>, Listener {

  private Interactor[] interactors = new Interactor[2];
  private Integer hashCode = 0;

  @Override
  public String getId() {
    return toString();
  }
  
  public String getIdWithoutDomains() {
	  String s = "";
	  s += first().getProtein();
	  s += " pp ";
	  s += second().getProtein();
	  return s;
  }

  /**
   * Returns the interacting proteins.
   *
   * @return the proteins
   */
  public Pair<Protein> getProteins() {
    return new Pair<Protein>(first().getProtein(), second().getProtein());
  }
  
  /**
   * Returns the other protein for a given protein.
   * 
   * @param p a protein
   * @return the other protein
   */
  public Protein getOtherProtein(Protein p) {
    Pair<Protein> pp = getProteins();
    if(pp.getFirst() == p)
      return pp.getSecond();
    return pp.getFirst();
  }

  /**
   * Add an interactor.
   *
   * @param interactor the interactor
   */
  public void addInteractor(Interactor interactor) {
    for(int i=0; i<2; i++) {
      if(interactors[i] == null) {
        interactors[i] = interactor;
        interactor.addListener(this);
        if(!isEmpty())
          update();
        return;
      }
    }
    System.err.println("Error: Interaction may only contain two proteins");
  }

  /**
   * Remove an interactor.
   *
   * @param interactor the interactor
   */
  public void removeInteractor(Interactor interactor) {
    for(int i=0; i<2; i++) {
      if(interactors[i] == interactor) {
        interactors[i] = null;
        interactor.removeListener(this);
        update();
        return;
      }
    }
  }

  /**
   * Iterate over the interactors.
   *
   * @return an iterator
   */
@Override
  public Iterator<Interactor> iterator() {
    return new ArrayIterator(interactors);
  }

  /**
   * Returns the first interactor.
   *
   * @return the interactor
   */
  public Interactor first() {
    return interactors[0];
  }

  /**
   * Returns the second interactor.
   *
   * @return the interactor
   */
  public Interactor second() {
    return interactors[1];
  }

  /**
   * Check whether an interactor is contained in this interaction.
   *
   * @param i the interactor
   * @return true if interactor is contained
   */
  public boolean contains(Interactor i) {
    for(Interactor j : this) {
      if(j.equals(i))
        return true;
    }
    return false;
  }

  /**
   * Checks whether a protein is taking part in this interaction.
   *
   * @param p the protein
   * @return true if protein is taking part
   */
  public boolean contains(Protein p) {
    for(Interactor j : this) {
      if(j.getProtein().equals(p))
        return true;
    }
    return false;
  }

  /**
   * Checks if two interactions have one interactor in common.
   *
   * @param i an interaction
   * @return true if two interactions have one interactor in common
   */
  public boolean intersects(Interaction i) {
    for(Interactor j : this) {
      if(i.contains(j))
        return true;
    }
    return false;
  }

  /**
   * Checks if interaction is empty.
   *
   * @return true if interaction is empty
   */
  public boolean isEmpty() {
    return interactors[0] == null || interactors[1] == null;
  }

  /**
   * Check if interaction is a protein with itself.
   * @return true if protein interacts with itself
   */
  public boolean isSelfInteraction() {
    return interactors[0].getProtein().equals(interactors[1].getProtein());
  }

  @Override
  public String toString() {
    if(interactors[0] != null && interactors[1] != null) {
      return interactors[0].toString() + " pp " + interactors[1].toString();
    }
    else
      return "empty interaction";
  }

  @Override
  public int compareTo(Interaction o) {
    if (equals(o)) {
      return 0;
    }

    return toString().compareTo(o.toString());
  }

  @Override
  public void update() {
    Arrays.sort(interactors);
    hashCode = toString().hashCode();
    updateListeners();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Interaction other = (Interaction) obj;
    if (!this.interactors[0].equals(other.interactors[0]) ||
            !this.interactors[1].equals(other.interactors[1])) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    if(hashCode == null)
      hashCode = toString().hashCode();
    return hashCode;
  }
}
