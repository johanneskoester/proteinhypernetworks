/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.constraints;

import modalLogic.formula.Formula;
import modalLogic.formula.io.FormulaFormatter;
import modalLogic.formula.io.HumanLanguageFormatter;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.util.Listener;

/**
 * A constraint, that puts a dependency onto a protein or interaction.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Constraint implements Comparable<Constraint>, Listener {

  private static boolean humanLanguageFormatting = true;
  private static FormulaFormatter<NetworkEntity> humanLanguageFormatter = new HumanLanguageFormatter<NetworkEntity>();

  /**
   * Sets whether constraint should be formatted in human readable language.
   *
   * @param b whether constraint should be formatted in human readable language
   */
  public static void setHumanLanguageFormatting(boolean b) {
    humanLanguageFormatting = b;
  }

  private Formula<NetworkEntity> implication;
  private Integer hashCode;

  /**
   * Returns the implied propositional logic formula.
   *
   * @return the implied propositional logic formula
   */
  public Formula<NetworkEntity> getImplication() {
    return implication;
  }

  /**
   * Returns the constrained network entity.
   *
   * @return the constrained network entity
   */
  public NetworkEntity getConstrainedNetworkEntity() {
    return implication.getChild(0).getProposition();
  }

  /**
   * Sets the implication.
   *
   * @param implication an implication formula
   */
  public void setImplication(Formula<NetworkEntity> implication) {
    if(this.implication != null) {
      for(NetworkEntity e : implication.getPropositions())
        e.removeListener(this);
    }
    this.implication = implication;
    for(NetworkEntity e : implication.getPropositions())
      e.addListener(this);
  }

  @Override
  public void update() {
    hashCode = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Constraint other = (Constraint) obj;
    if (hashCode() != other.hashCode()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    if(hashCode == null)
      hashCode = this.implication.toString().hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    if(implication == null) {
      return "empty constraint";
    }
    if(humanLanguageFormatting)
      return humanLanguageFormatter.transform(implication);
    else
      return implication.toString();
  }

  @Override
  public int compareTo(Constraint o) {
    if (equals(o)) {
      return 0;
    }

    return this.implication.toString().compareTo(o.implication.toString());
  }
}
