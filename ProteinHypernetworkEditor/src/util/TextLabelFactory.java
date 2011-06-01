/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

import org.apache.commons.collections15.Factory;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class TextLabelFactory implements Factory<TextLabel>{
  private String text;

  public TextLabelFactory(String text) {
    this.text = text;
  }

  @Override
  public TextLabel create() {
    return new TextLabel(text);
  }


}
