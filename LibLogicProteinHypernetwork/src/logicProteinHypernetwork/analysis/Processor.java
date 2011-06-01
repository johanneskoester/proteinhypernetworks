/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package logicProteinHypernetwork.analysis;

import util.ProgressBean;

/**
 * Abstract class Processor is a template for all classes that perform a
 * processing task. Allows progress monitoring.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class Processor {

  protected ProgressBean progressBean = new ProgressBean();

  /**
   * Returns the progress monitor bean.
   *
   * @return the progress monitor bean
   */
  public ProgressBean getProgressBean() {
    return progressBean;
  }

  /**
   * Listen to the progress of another processor.
   *
   * @param p the other processor
   */
  public void listen(Processor p) {
    progressBean.addPropertyChangeListener(p.progressBean);
  }

  /**
   * Perform the processing task.
   */
  public abstract void process();

  /**
   * Class that manages the left steps todo.
   */
  protected class ToDo {
    int done;
    int todo;

    /**
     * Constructor of class ToDo
     * 
     * @param todo the initial steps to do
     */
    public ToDo(int todo) {
      this.todo = todo;
    }

    /**
     * Returns the number of done steps.
     *
     * @return the number of done steps
     */
    public int getDone() {
      return done;
    }

    /**
     * Sets the number of done steps.
     *
     * @param done the number of done steps
     */
    public void setDone(int done) {
      this.done = done;
    }

    /**
     * Returns the overall steps to do.
     *
     * @return the overall steps to do
     */
    public int getTodo() {
      return todo;
    }

    /**
     * Marks the next step as done.
     */
    public void increaseDone() {
      done++;
    }
  }
}
