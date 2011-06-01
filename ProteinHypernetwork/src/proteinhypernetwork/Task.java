/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetwork;

import org.jdesktop.application.Application;
import org.jdesktop.application.TaskEvent;
import org.jdesktop.application.TaskListener;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public abstract class Task extends org.jdesktop.application.Task {

  public Task() {
    super(Application.getInstance());
    this.addTaskListener(new TaskListener() {

      @Override
      public void doInBackground(TaskEvent event) {
        Application.getInstance().getContext().getTaskMonitor().setForegroundTask(Task.this);
      }

      @Override
      public void process(TaskEvent event) {
      }

      @Override
      public void succeeded(TaskEvent event) {
      }

      @Override
      public void failed(TaskEvent event) {
      }

      @Override
      public void cancelled(TaskEvent event) {
      }

      @Override
      public void interrupted(TaskEvent event) {
      }

      @Override
      public void finished(TaskEvent event) {
      }
    });
  }
}
