/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates.minimalNetworkStatesToNetworkStates;

/**
 * A removal instruction in the instruction tree.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Instruction {

  public int remove;
  public Instruction[] children;
  public boolean marked = false;

  /**
   * Constructor of class instruction.
   * 
   * @param remove the item to remove
   */
  public Instruction(int remove) {
    this.remove = remove;
  }

  /**
   * Mark this node.
   */
  public void mark() {
    marked = true;
  }

  /**
   * Set the number of children.
   *
   * @param count the number of children
   */
  public void setChildren(int count) {
    children = new Instruction[count];
  }

  /**
   * Add a child.
   *
   * @param child the child
   */
  public void addChild(Instruction child) {
    int i = 0;
    if (children[0] != null) {
      i++;
    }
    children[i] = child;
  }

  /**
   * Remove a child.
   *
   * @param child the child
   */
  public void removeChild(Instruction child) {
    if (children == null) {
      System.out.println("null");
    }
    for (int i = 0; i < children.length; i++) {
      if (children[i] == child) {
        if (children.length == 1) {
          children = null;
          return;
        } else {
          Instruction j = children[(i + 1) % 2];
          children = new Instruction[1];
          children[0] = j;
        }

      }
    }
  }

  /**
   * Returns true if node is marked.
   *
   * @return true if node is marked
   */
  public boolean isMarked() {
    return marked;
  }

  /**
   * Returns true if node is leaf.
   *
   * @return true if node is leaf
   */
  public boolean isLeaf() {
    return children == null;
  }

  /**
   * Returns true if node is last / rightmost child of parent.
   *
   * @param c the parent node
   * @return true if node is last / rightmost child of parent
   */
  public boolean isLastChild(Instruction c) {
    return children.length == 1 || children[1] == c;
  }
}
