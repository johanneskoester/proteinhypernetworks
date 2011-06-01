/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

import javax.swing.JLabel;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class TextLabel extends JLabel {

  public TextLabel(String text) {
    super(text);
    setPreferredSize(getMaximumSize());
  }
}
