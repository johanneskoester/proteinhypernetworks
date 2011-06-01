/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;

/**
 * Decodes a protein hypernetwork without constraints from a PSIMI 2.5 file.
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class PSIMI25Decoder implements Decoder {

  @Override
  public void decode(File file, ProteinHypernetwork proteinHypernetwork) throws Exception {
    XMLInputFactory fac = XMLInputFactory.newInstance();

    InputStream input = new FileInputStream(file);
    XMLStreamReader reader = fac.createXMLStreamReader(input);

    Interaction interaction = null;
    String id = null;
    String iid = "";
    Interactor interactor = null;

    while(reader.hasNext()) {
      reader.next();
      String tag = reader.getLocalName();
      if(reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
        if(tag.equals("interaction")) {
          interaction = new Interaction();
          iid = reader.getAttributeValue(0);
        }

        if(tag.equals("interactor")) {
          interactor = new Interactor();
        }

        if(interactor != null && id == null && tag.equals("shortLabel")) {
          id = reader.getElementText();
        }
      }

      else if(reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
        if(tag.equals("interaction")) {
          if(!interaction.isEmpty()) {
            if(!proteinHypernetwork.getInteractions().contains(interaction))
              proteinHypernetwork.getInteractions().add(interaction);
          }
          interaction = null;
        }

        if(tag.equals("interactor")) {
          /**
           * @todo this is an O(|P|) call to getProteinWithId.
           * Replace this with an efficient map or update the buildIndex
           */
          Protein p = proteinHypernetwork.getProteins().getProteinById(id);

          if(p == null) {
            p = new Protein(id);
            proteinHypernetwork.getProteins().add(p);
          }

          interactor.setProtein(p);

          interaction.addInteractor(interactor);
          interactor = null;
          id = null;
        }
      }
    }
  }


}
