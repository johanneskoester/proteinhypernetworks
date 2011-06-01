/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.proteins;

import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.util.AbstractListenable;

/**
 * A protein.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Protein extends AbstractListenable implements NetworkEntity,Comparable<Protein>{
  private String id = "";

  /**
   * Constructs an empty protein without id.
   */
  public Protein() {
    
  }

  /**
   * Constructor of class Protein.
   *
   * @param id the id of the protein
   */
  public Protein(String id) {
    this.id = id;
  }

  /**
   * Sets the id of the protein.
   *
   * @param id the id
   */
  public void setId(String id) {
    this.id = id;
    updateListeners();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public int compareTo(Protein o) {
    if(equals(o))
      return 0;

    return toString().compareTo(o.toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Protein other = (Protein) obj;
    if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

}
