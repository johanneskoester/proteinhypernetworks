/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class LimitedExecutor {
  
  private ExecutorService executor;

  /**
   * Constructor of class limited executor.
   *
   * @param simultaneousThreads number of simultaneous threads
   */
  public LimitedExecutor(int simultaneousThreads) {
    executor = Executors.newFixedThreadPool(simultaneousThreads);
  }

  /**
   * Submits a runnable.
   *
   * @param runnable the runnable
   */
  public void submit(Runnable runnable) {
    executor.submit(runnable);
  }

  /**
   * Await the termination of all runnables.
   */
  public void awaitTermination() {
    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (InterruptedException ex) {
      System.err.println(ex);
    }
  }
}
