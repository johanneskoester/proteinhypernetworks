/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.interactions;

import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.AbstractListenable;
import proteinHypernetwork.util.Listener;

/**
 * An interactor is a protein or a protein domain.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Interactor extends AbstractListenable implements Comparable<Interactor>, Listener {

  private Protein protein;
  private String domain;
  private Integer hashCode;

  /**
   * Constructor for an empty interactor.
   */
  public Interactor() { 
  }

  /**
   * Constructor of class Interactor.
   *
   * @param protein the protein
   */
  public Interactor(Protein protein) {
    setProtein(protein);
  }

  /**
   * Returns the protein.
   *
   * @return the protein
   */
  public Protein getProtein() {
    return protein;
  }

  /**
   * Sets the protein.
   *
   * @param protein the protein
   */
  public void setProtein(Protein protein) {
    if(this.protein != null)
      this.protein.removeListener(this);
    this.protein = protein;
    this.protein.addListener(this);
    update();
  }

  /**
   * Returns the domain.
   *
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * Sets the domain.
   *
   * @param domain the domain
   */
  public void setDomain(String domain) {
    this.domain = domain;
    update();
  }

  /**
   * Checks whether the interactor contains a domain.
   *
   * @return true if a domain is referenced
   */
  public boolean isDomainInteractor() {
    return domain != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Interactor other = (Interactor) obj;
    if (hashCode() != other.hashCode()) {
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

  @Override
  public String toString() {
    String toString = "";
      if(protein != null)
        toString = protein.toString();
      else
        return "";
      if (domain != null) {
        toString += "[" + domain + "]";
      }
    return toString;
  }

  @Override
  public int compareTo(Interactor o) {
    if (equals(o)) {
      return 0;
    }

    return toString().compareTo(o.toString());
  }

  @Override
  public void update() {
    hashCode = null;
    updateListeners();
  }
}
