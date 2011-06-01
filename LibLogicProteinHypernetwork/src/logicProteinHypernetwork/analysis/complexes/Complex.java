/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import org.apache.commons.collections15.SetUtils;
import proteinHypernetwork.proteins.Protein;

/**
 * Class Complex models a list of proteins that are in one complex.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Complex extends ArrayList<Protein> implements Collection<Protein>, Comparable<Complex> {

  private ProteinSubnetwork subnetwork;
  private int hashCode = -1;

  /**
   * Construct an empty complex.
   */
  public Complex() {
  }

  /**
   * Construct a complex out of an iterable of proteins.
   *
   * @param c the proteins
   */
  public Complex(Iterable<? extends Protein> c)  {
    for(Protein p : c)
      add(p);
  }

  /**
   * Add a protein to the complex.
   *
   * @param p the protein
   * @return true
   */
  @Override
  public boolean add(Protein p) {
    boolean b = super.add(p);
    hashCode = -1;
    return b;
  }

  /**
   * Returns the protein subnetwork corresponding to this complex.
   *
   * @return the protein subnetwork
   */
  public ProteinSubnetwork getSubnetwork() {
    return subnetwork;
  }

  /**
   * Sets the protein subnetwork.
   *
   * @param subnetwork the subnetwork
   */
  public void setSubnetwork(ProteinSubnetwork subnetwork) {
    this.subnetwork = subnetwork;
  }

  public int compareTo(Complex o) {
    return o.size() - size();
  }

  @Override
  public int hashCode() {
    if (hashCode == -1) {
      hashCode = 0;
      for (Protein p : this) {
        hashCode += p.hashCode(); // the sum of hashcodes as specified in the set interface
      }
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Complex other = (Complex) obj;

    return SetUtils.isEqualSet(this, other);
  }

  @Override
  public String toString() {
    String s = "";
    for (int i = 0; i < this.size(); i++) {
      s += this.get(i);
      if (i < this.size() - 1) {
        s += ", ";
      }
    }
    return s;
  }

  /**
   * Comparator that compares complexes by their density.
   */
  public static class DensityComparator implements Comparator<Complex> {

    private ProteinSubnetwork.DensityComparator subcomp = new ProteinSubnetwork.DensityComparator();

    public int compare(Complex c, Complex c1) {
      return subcomp.compare(c.subnetwork, c1.subnetwork);
    }
  }
}
