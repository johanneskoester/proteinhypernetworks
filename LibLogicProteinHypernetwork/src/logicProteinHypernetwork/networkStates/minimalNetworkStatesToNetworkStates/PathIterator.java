/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.networkStates.minimalNetworkStatesToNetworkStates;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * An iterator over InstructionTree branches.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class PathIterator implements Iterator<Iterator<Instruction>> {

  private Stack<Instruction> stack = new Stack<Instruction>();
  private boolean visitEmptyPath = true;
  private Instruction tree;

  /**
   * Constructor of class PathIterator.
   * 
   * @param tree the instruction tree
   */
  public PathIterator(Instruction tree) {
    this.tree = tree;
    stack.push(tree);
    InstructionTree.unmark(tree);
  }

  @Override
  public boolean hasNext() {
    if (stack.empty()) {
      return false;
    }

    if (tree.children == null) {
      return visitEmptyPath;
    }

    for (Instruction n : tree.children) {
      if (!n.isMarked()) {
        return true;
      }
    }

    // no children or all marked
    return false;
  }

  @Override
  public Iterator<Instruction> next() {
    if (tree.children == null) {
      visitEmptyPath = false;
      // return the empty path
      return new Iterator<Instruction>() {

        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public Instruction next() {
          throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException("Not supported.");
        }
      };
    }

    /**
     * Attention: only one of these iterators may be iterated at a time!!!
     */
    return new Iterator<Instruction>() {

      private boolean hasNext = true;

      @Override
      public boolean hasNext() {
        return hasNext;
      }

      @Override
      public Instruction next() {
        for (Instruction n : stack.peek().children) {
          if (!n.isMarked()) {
            if (n.isLeaf()) {
              n.mark();

              Instruction m = n;
              try {
                while (!stack.empty() && stack.peek().isLastChild(m)) {
                  // mark parent if this is the second marked leaf
                  m = stack.pop();
                  m.mark();
                }
              } catch (NullPointerException e) {
                e.printStackTrace();
              }

              stack.clear();
              stack.push(tree);
              hasNext = false;
            } else {
              // not a leaf
              stack.push(n);
            }
            return n;
          }
        }

        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported.");
      }
    };
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Not supported.");
  }
}
