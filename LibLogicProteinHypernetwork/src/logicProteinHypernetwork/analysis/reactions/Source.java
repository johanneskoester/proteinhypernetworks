/* Copyright (c) 2012, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.reactions;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import proteinHypernetwork.proteins.Protein;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Source extends Reaction {

  public Source(Protein p) {
    super(null, null, new ComplexMultigraph(p));
  }

  @Override
  public void toSBML(XMLStreamWriter xmlw, int id) throws XMLStreamException {
    // do nothing since this is not really a reaction
  }
  
  
}
