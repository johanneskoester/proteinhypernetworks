/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.encoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import modalLogic.formula.io.MathMLWriter;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;
import proteinHypernetwork.util.Listener;

/**
 * Encodes a protein hypernetwork into an output file.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class HypernetworkMLEncoder implements Encoder {

  public static String HYPERNETWORK = "hypernetwork";
  public static String ID = "id";
  public static String GRAPH = "graph";
  public static String NODE = "node";
  public static String PORT = "port";
  public static String NAME = "name";
  public static String EDGE = "edge";
  public static String SOURCE = "source";
  public static String TARGET = "target";
  public static String SOURCEPORT = "sourceport";
  public static String TARGETPORT = "targetport";
  public static String[] SOURCETARGET = {SOURCE, TARGET};
  public static String[] SOURCETARGETPORT = {SOURCEPORT, TARGETPORT};
  public static String CONSTRAINTS = "constraints";

  @Override
  public void encode(ProteinHypernetwork proteinHypernetwork, File file) throws XMLStreamException, FileNotFoundException {
    // Create an output factory
    XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
    // Set namespace prefix defaulting for all created writers
    //xmlof.setProperty("javax.xml.stream.isPrefixDefaulting",Boolean.TRUE);

    // Create an XML stream writer
    XMLStreamWriter xmlw =
            xmlof.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(file)));

    // Write XML prologue
    xmlw.writeStartDocument("UTF-8", "1.0");

    xmlw.writeStartElement("", HYPERNETWORK, "http://www.rahmannlab.de/research/hypernetworks/hypernetworkml");
    xmlw.writeAttribute("xmlns", "http://www.rahmannlab.de/research/hypernetworks/hypernetworkml");

    xmlw.writeStartElement("", GRAPH, "http://graphml.graphdrawing.org/xmlns");
    xmlw.writeAttribute("xmlns", "http://graphml.graphdrawing.org/xmlns");
    for (Protein p : proteinHypernetwork.getProteins()) {
      writeProtein(xmlw, p);
    }

    for (Interaction i : proteinHypernetwork.getInteractions()) {
      writeInteraction(xmlw, i);
    }
    xmlw.writeEndElement();

    xmlw.writeStartElement("", CONSTRAINTS, "http://www.w3.org/1998/Math/MathML");
    xmlw.writeAttribute("xmlns", "http://www.w3.org/1998/Math/MathML");
    MathMLWriter<NetworkEntity> mmlw = new MathMLWriter<NetworkEntity>(xmlw);
    for (Constraint c : proteinHypernetwork.getConstraints()) {
      writeConstraint(mmlw, c);
    }
    xmlw.writeEndElement();

    xmlw.writeEndElement();
    xmlw.writeEndDocument();

    xmlw.flush();
    xmlw.close();
  }

  private void writeProtein(XMLStreamWriter xmlw, Protein p) throws XMLStreamException {
    Set<String> domains = new HashSet<String>();
    Iterator<Listener> ls = p.listenerIterator();
    while (ls.hasNext()) {
      Listener l = ls.next();
      if (l instanceof Interactor) {
        Interactor i = (Interactor) l;
        if (i.isDomainInteractor()) {
          domains.add(i.getDomain());
        }
      }
    }

    boolean hasDomains = !domains.isEmpty();

    if (hasDomains) {
      xmlw.writeStartElement(NODE);
    } else {
      xmlw.writeEmptyElement(NODE);
    }
    
    xmlw.writeAttribute(ID, p.getId());

    if (hasDomains) {
      for (String d : domains) {
        xmlw.writeStartElement(PORT);
        xmlw.writeAttribute(NAME, d);
        xmlw.writeEndElement();
      }
      xmlw.writeEndElement();
    }
  }

  private void writeInteraction(XMLStreamWriter xmlw, Interaction i) throws XMLStreamException {
    xmlw.writeEmptyElement(EDGE);
    xmlw.writeAttribute(ID, i.getId());
    int k = 0;
    for (Interactor d : i) {
      xmlw.writeAttribute(SOURCETARGET[k], d.getProtein().getId());
      if (d.isDomainInteractor()) {
        xmlw.writeAttribute(SOURCETARGETPORT[k], d.getDomain());
      }
      k++;
    }
  }

  private void writeConstraint(MathMLWriter mmlw, Constraint c) throws XMLStreamException {
    mmlw.write(c.getImplication());
  }
}
