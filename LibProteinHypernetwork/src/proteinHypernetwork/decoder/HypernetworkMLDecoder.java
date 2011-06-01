/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.decoder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import modalLogic.formula.Formula;
import modalLogic.formula.io.MathMLReader;
import modalLogic.formula.io.MathMLReader.PropositionMap;
import modalLogic.formula.io.MathMLWriter;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.encoder.HypernetworkMLEncoder;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;

/**
 * Decodes a protein hypernetwork from a HypernetworkML (.hml) input file.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class HypernetworkMLDecoder implements Decoder {

  @Override
  public void decode(File file, ProteinHypernetwork proteinHypernetwork) throws XMLStreamException, FileNotFoundException {
    XMLInputFactory fac = XMLInputFactory.newInstance();

    XMLEventReader xmlr = fac.createXMLEventReader(new BufferedInputStream(new FileInputStream(file)));


    Map<String, Protein> proteins = new HashMap<String, Protein>();
    Map<String, Interaction> interactions = new HashMap<String, Interaction>();
    Collection<Constraint> constraints = new ArrayList<Constraint>();

    while (xmlr.hasNext()) {
      XMLEvent e = xmlr.nextEvent();

      if (e.isStartElement()) {
        StartElement s = e.asStartElement();

        if (s.getName().getLocalPart().equals(HypernetworkMLEncoder.NODE)) {
          readProtein(s, proteins);
        } else if (s.getName().getLocalPart().equals(HypernetworkMLEncoder.EDGE)) {
          readInteraction(s, proteins, interactions);
        } else if (s.getName().getLocalPart().equals(HypernetworkMLEncoder.CONSTRAINTS)) {
          readConstraints(xmlr, proteins, interactions, constraints);
        }
      }
    }

    proteinHypernetwork.getProteins().addAll(proteins.values());
    proteinHypernetwork.getInteractions().addAll(interactions.values());
    proteinHypernetwork.getConstraints().addAll(constraints);

    Collections.sort(proteinHypernetwork.getProteins());
    Collections.sort(proteinHypernetwork.getInteractions());

    xmlr.close();
  }
  
  private void readProtein(StartElement s, Map<String, Protein> proteins) throws XMLStreamException {
    String id = s.getAttributeByName(new QName(HypernetworkMLEncoder.ID)).getValue();
    proteins.put(id, new Protein(id));
  }

  private void readInteraction(StartElement s, Map<String, Protein> proteins, Map<String, Interaction> interactions) throws XMLStreamException {
    Interaction i = new Interaction();
    for(int k=0; k<2; k++) {
      Attribute interactorId = s.getAttributeByName(new QName(HypernetworkMLEncoder.SOURCETARGET[k]));
      Interactor j = new Interactor();
      j.setProtein(proteins.get(interactorId.getValue()));
      
      Attribute domain = s.getAttributeByName(new QName(HypernetworkMLEncoder.SOURCETARGETPORT[k]));
      if(domain != null)
        j.setDomain(domain.getValue());

      i.addInteractor(j);
    }

    Attribute id = s.getAttributeByName(new QName(HypernetworkMLEncoder.ID));
    String ids = null;
    if(id == null)
      ids = i.getId();
    else
      ids = id.getValue();
    interactions.put(ids, i);
  }

  private void readConstraints(XMLEventReader xmlr, final Map<String, Protein> proteins,
          final Map<String, Interaction> interactions, Collection<Constraint> constraints) throws XMLStreamException {

    MathMLReader<NetworkEntity> mmlr = new MathMLReader<NetworkEntity>(xmlr, new PropositionMap<NetworkEntity>(){

      @Override
      public NetworkEntity get(String s) {
        if(proteins.containsKey(s))
          return proteins.get(s);
        return interactions.get(s);
      }
      
    });

    while (xmlr.hasNext()) {
      XMLEvent e = xmlr.nextEvent();

      if (e.isStartElement()) {
        StartElement s = e.asStartElement();
        if (s.getName().getLocalPart().equals(MathMLWriter.APPLY)) {
          Formula<NetworkEntity> formula = mmlr.read();
          if(formula.getType() == Formula.IMPLICATION) {
            Constraint c = new Constraint();
            c.setImplication(formula);
            constraints.add(c);
          }
          else
            throw new XMLStreamException("A constraint has to be an implication.");
        }
      }
    }
  }

}
