/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package proteinHypernetwork.decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.Interactor;
import proteinHypernetwork.proteins.Protein;

/**
 * Decodes a protein hypernetwork (whithout constraints) from a cytoscape network
 * file (.sif).
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class SifDecoder implements Decoder {

  @Override
  public void decode(File file, ProteinHypernetwork proteinHypernetwork) throws FileNotFoundException {
    Map<String, Protein> proteins = new HashMap<String, Protein>();
    Set<Interaction> interactions = new TreeSet<Interaction>();

    Scanner s = new Scanner(file);
    while(s.hasNext()) {
      String p1 = s.next();
      String edge = s.next();
      String p2 = s.next();
      if(edge.equals("pp")) {

        Protein prot1 = proteins.get(p1);
        Protein prot2 = proteins.get(p2);
        if(prot1 == null) {
          prot1 = new Protein();
          prot1.setId(p1);
          proteins.put(prot1.getId(), prot1);
        }
        if(prot2 == null) {
          prot2 = new Protein();
          prot2.setId(p2);
          proteins.put(prot2.getId(), prot2);
        }

        Interaction i = new Interaction();
        i.addInteractor(new Interactor(prot1));
        i.addInteractor(new Interactor(prot2));
        boolean isNew = interactions.add(i);
        if(!isNew)
          System.err.println("Duplicate interaction filtered out: " + i);
      }
    }

    proteinHypernetwork.getProteins().addAll(proteins.values());
    proteinHypernetwork.getInteractions().addAll(interactions);
  }

}
