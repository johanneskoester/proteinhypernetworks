/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinHypernetwork.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Decodes a protein hypernetwork from an input file.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class MITAB26Decoder implements Decoder {

  /**
   * Decode a protein hypernetwork.
   *
   * @param file the input file
   * @param proteinHypernetwork the resulting protein hypernetwork
   * @throws Exception an exception while decoding
   */
  @Override
  public void decode(File file, ProteinHypernetwork proteinHypernetwork) throws Exception {
    Map<String, Protein> proteins = new HashMap<String, Protein>();
    Set<Interaction> interactions = new TreeSet<Interaction>();

    BufferedReader reader  = new BufferedReader(new FileReader(file));
    String line = null;
    while((line = reader.readLine()) != null) {
      // TODO
    }
  }
}
