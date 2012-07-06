/* Copyright (c) 2012, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.reactions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Reaction {

  ComplexMultigraph complex;
  ComplexMultigraph ligand;
  ComplexMultigraph product;

  public Reaction(ComplexMultigraph complex, ComplexMultigraph ligand, ComplexMultigraph targetComplex) {
    this.complex = complex;
    this.ligand = ligand;
    this.product = targetComplex;
  }
  
  public static void writeSBML(List<Reaction> reactions, File file) throws XMLStreamException, FileNotFoundException {
    String compartment = "default";
    XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
    XMLStreamWriter xmlw =
            xmlof.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(file)));
    xmlw.writeStartDocument("UTF-8", "1.0");
    {
      xmlw.writeStartElement("sbml");
      xmlw.writeAttribute("xmlns", "http://www.sbml.org/sbml/level2/version4");
      xmlw.writeAttribute("level", "2");
      xmlw.writeAttribute("version", "4");
      {
        xmlw.writeStartElement("model");
        {
          xmlw.writeStartElement("listOfCompartments");
          {
            xmlw.writeEmptyElement("compartment");
            xmlw.writeAttribute("id", compartment);
            xmlw.writeAttribute("size", "1");
          }
          xmlw.writeEndElement();
          xmlw.writeStartElement("listOfSpecies");
          {
            for(Reaction r : reactions) {
              xmlw.writeEmptyElement("species");
              xmlw.writeAttribute("id", toSId(r.product));
              xmlw.writeAttribute("compartment", compartment);
              xmlw.writeAttribute("initialAmount", "1");
            }
          }
          xmlw.writeEndElement();
          
          xmlw.writeStartElement("listOfParameters");
          {
            for(int i=0; i<reactions.size(); i++) {
              xmlw.writeEmptyElement("parameter");
              xmlw.writeAttribute("id", "k_on_" + i);
              xmlw.writeEmptyElement("parameter");
              xmlw.writeAttribute("id", "k_off_" + i);
            }
          }
          xmlw.writeEndElement();
          
          xmlw.writeStartElement("listOfReactions");
          {
            for(int i=0; i<reactions.size(); i++) {
              reactions.get(i).toSBML(xmlw, i);
            }
          }
          xmlw.writeEndElement();
        }
        xmlw.writeEndElement();
      }
      xmlw.writeEndElement();
    }
    xmlw.writeEndDocument();

    xmlw.flush();
    xmlw.close();
  }

  public void toSBML(XMLStreamWriter xmlw, int id) throws XMLStreamException {
    xmlw.writeStartElement("reaction");
    xmlw.writeAttribute("id", "_"+ id);
    {

      xmlw.writeStartElement("listOfReactants");
      xmlw.writeEmptyElement("speciesReference");
      xmlw.writeAttribute("species", toSId(complex));
      xmlw.writeEmptyElement("speciesReference");
      xmlw.writeAttribute("species", toSId(ligand));
      xmlw.writeEndElement();
      
      xmlw.writeStartElement("listOfProducts");
      xmlw.writeEmptyElement("speciesReference");
      xmlw.writeAttribute("species", toSId(product));
      xmlw.writeEndElement();

      xmlw.writeStartElement("kineticLaw");
      {
        xmlw.writeStartElement("math");
        xmlw.writeAttribute("xmlns", "http://www.w3.org/1998/Math/MathML");
        {

          xmlw.writeStartElement("apply");
          {
            xmlw.writeEmptyElement("minus");

            xmlw.writeStartElement("apply");
            {
              xmlw.writeEmptyElement("times");

              xmlw.writeStartElement("ci");
              xmlw.writeCData("k_on_" + id);
              xmlw.writeEndElement();

              xmlw.writeStartElement("ci");
              xmlw.writeCData(toSId(complex));
              xmlw.writeEndElement();

              xmlw.writeStartElement("ci");
              xmlw.writeCData(toSId(ligand));
              xmlw.writeEndElement();
            }
            xmlw.writeEndElement();

            xmlw.writeStartElement("apply");
            {
              xmlw.writeEmptyElement("times");

              xmlw.writeStartElement("ci");
              xmlw.writeCData("k_off_" + id);
              xmlw.writeEndElement();

              xmlw.writeStartElement("ci");
              xmlw.writeCData(toSId(product));
              xmlw.writeEndElement();
            }
            xmlw.writeEndElement();

          }
          xmlw.writeEndElement();

        }
        xmlw.writeEndElement();

      }
      xmlw.writeEndElement();

    }
    xmlw.writeEndElement();
  }
  
  @Override
  public String toString() {
    return "d/dt * [" + toSId(product) + "] = k_on["  + toSId(complex) + "] * [" + toSId(ligand) + "] - k_off[" + toSId(product) + "]";
  }
  
  public static String toSId(ComplexMultigraph complex) {
    return complex.toString().replaceAll("[^\\w]", "_");
  }
}
