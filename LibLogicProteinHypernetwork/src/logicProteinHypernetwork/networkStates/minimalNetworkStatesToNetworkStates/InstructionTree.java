/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates.minimalNetworkStatesToNetworkStates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import logicProteinHypernetwork.networkStates.MinimalNetworkState;

/**
 * InstructionTree calculates a tree of removal instructions to turn a set
 * of minimal network states clash free.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class InstructionTree {

  private MinimalNetworkState[] states;
  private long leafs = 1;
  private Instruction tree = new Instruction(-1);
  private ArrayList<Integer>[] clashes;

  /**
   * Constructor of class InstructionTree.
   *
   * @param states the minimal network states
   */
  public InstructionTree(MinimalNetworkState[] states) {
    this.states = states;
    clashes = new ArrayList[states.length];
    buildTree();
    removeInvalid();
  }

  /**
   * Returns the tree.
   *
   * @return the root of the tree
   */
  public Instruction getTree() {
    return tree;
  }

  /**
   * Returns the number of leafs.
   * 
   * @return the number of leafs of the tree
   */
  public long getLeafs() {
    return leafs;
  }

  /**
   * Returns the minimal network states.
   *
   * @return the minimal network states
   */
  public MinimalNetworkState[] getStates() {
    return states;
  }

  /**
   * Returns an iterator over not clashing maximal combinations of minimal network
   * states, i.e. network states.
   *
   * @return the iterator
   */
  public NetworkStateIterator getNetworkStateIterator() {
    return new NetworkStateIterator(this);
  }

  /**
   * Recursively unmark a subtree.
   *
   * @param i the root of the subtree
   */
  static void unmark(Instruction i) {
    i.marked = false;
    if (!i.isLeaf()) {
      for (Instruction j : i.children) {
        unmark(j);
      }
    }
  }

  /**
   * Build the removal instruction tree.
   */
  private void buildTree() {
    for (int i = 0; i < clashes.length; i++) {
      clashes[i] = new ArrayList<Integer>();
    }

    for (int i = 0; i < states.length; i++) {
      MinimalNetworkState w = states[i];
      for (int j = i + 1; j < states.length; j++) {
        MinimalNetworkState v = states[j];
        if (w.isClash(v)) {
          clashes[i].add(j);
          clashes[j].add(i);
        }
      }
    }

    for (int i = 0; i < clashes.length; i++) {
      if (!clashes[i].isEmpty()) {
        appendToLeafs(tree, i, clashes[i]);
      }
    }
  }

  /**
   * Append a new instruction to the leafs.
   *
   * @param n the root of the subtree
   * @param remove1 item to remove
   * @param remove2 items to remove else
   */
  private void appendToLeafs(Instruction n, int remove1, List<Integer> remove2) {
    appendToLeafs(n, remove1, remove2, new boolean[remove2.size()]);
  }

  /**
   * Append a new instruction to the leafs.
   *
   * @param n the root of the subtree
   * @param remove1 item to remove
   * @param remove2 items to remove else
   * @param notRemove2 items already removed
   */
  private void appendToLeafs(Instruction n, int remove1, List<Integer> remove2, boolean[] notRemove2) {
    if (n.isLeaf()) {
      n.setChildren(2);
      n.addChild(new Instruction(remove1));

      for (int i = 0; i < remove2.size(); i++) {
        if (!notRemove2[i]) {
          Instruction child = new Instruction(remove2.get(i));
          if (n.isLeaf()) {
            n.setChildren(1);
          }
          n.addChild(child);
          n = child;
          /*if (i < remove2.size() - 1) {
          n = child;
          n.setChildren(1);
          }*/
        }
      }
      leafs++;
      return;
    }

    // recursion
    for (Instruction c : n.children) {
      if (c.remove != remove1) {
        boolean[] newNotRemove2 = notRemove2;
        int r = remove2.indexOf(c.remove);
        if (r > -1) {
          newNotRemove2 = Arrays.copyOf(notRemove2, notRemove2.length);
          newNotRemove2[r] = true;
        }

        if (!isTrue(newNotRemove2)) {
          appendToLeafs(c, remove1, remove2, newNotRemove2);
        }
      }
    }
  }

  /**
   * Checks if a given bitvector contains only ones.
   *
   * @param bitvector the bitvector
   * @return whether bitvector contains only ones
   */
  private boolean isTrue(boolean[] bitvector) {
    for (boolean b : bitvector) {
      if (!b) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether a given bitvector contains only zeros.
   *
   * @param bitvector the bitvector
   * @return whether the bitvector contains only zeros
   */
  private boolean isFalse(boolean[] bitvector) {
    for (boolean b : bitvector) {
      if (b) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns iterator over branches in the tree.
   * 
   * @return an iterator
   */
  public Iterator<Iterator<Instruction>> pathIterator() {
    return new PathIterator(tree);
  }

  /**
   * Remove invalid branches
   */
  public void removeInvalid() {
    if (!tree.isLeaf()) {
      for (Instruction i : tree.children) {
        removeInvalid(i, new boolean[states.length]);
      }
    }
  }

  /**
   * Removes invalid branches.
   *
   * @param i the root of the subtree
   * @param visited visited nodes
   * @return whether i has to be removed from its parent
   */
  private boolean removeInvalid(Instruction i, boolean[] visited) {
    visited[i.remove] = true;

    int reason = -1;
    int notvisited = -1;
    boolean clashLeft = true;
    for (int m = 0; m < states.length; m++) {
      if (visited[m]) {
        boolean cl = false;
        for (int c : clashes[m]) {
          if (!visited[c]) {
            cl = true;
            break;
          }
        }
        if (!cl) {
          clashLeft = false;
          reason = m;
          break;
        }
      }
    }
    if (!clashLeft) {
      return true;
      /*for(Instruction k : tree.children)
      checkTree(k, new boolean[states.length]);*/
    } else if (!i.isLeaf()) {
      for (Instruction j : Arrays.copyOf(i.children, i.children.length)) {
        boolean[] visited2 = Arrays.copyOf(visited, visited.length);
        boolean remove = removeInvalid(j, visited2);
        if (remove) {
          if (i.children.length == 1) {
            return true; // remove i from parent if j is only child
          }
          i.removeChild(j); // remove child j
        }
      }
    }
    return false;
  }

  /**
   * Checks the tree.
   *
   * @param i the root of the subtree
   * @param visited visited nodes
   */
  private void checkTree(Instruction i, boolean[] visited) {
    visited[i.remove] = true;
    if (i.isLeaf()) {
      for (int m = 0; m < states.length; m++) {
        if (!visited[m]) {
          boolean clashLeft = false;
          int clashing = -1;
          for (int c : clashes[m]) {
            if (!visited[c]) {
              clashLeft = true;
              clashing = c;
              break;
            }
          }
          if (clashLeft) {
            System.err.println("Error: clash in tree: " + m + " " + clashing);
          }
        }
      }
    } else {
      for (Instruction j : i.children) {
        boolean[] visited2 = Arrays.copyOf(visited, visited.length);
        checkTree(j, visited2);
      }
    }
  }
}
