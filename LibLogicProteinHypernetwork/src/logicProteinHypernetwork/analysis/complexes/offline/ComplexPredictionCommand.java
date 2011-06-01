/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.complexes.offline;

import edu.uci.ics.jung.graph.UndirectedGraph;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import logicProteinHypernetwork.analysis.complexes.Complex;
import org.apache.commons.collections15.Transformer;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;

/**
 * Predict complexes with external command.
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class ComplexPredictionCommand implements Transformer<UndirectedGraph<Protein, Interaction>, Collection<Complex>> {

  private String command;
  private ProteinHypernetwork hypernetwork;

  /**
   * Constructor of ComplexPredictionCommand.
   *
   * @param command the external command
   * @param hypernetwork the protein hypernetwork
   */
  public ComplexPredictionCommand(String command, ProteinHypernetwork hypernetwork) {
    this.command = command;
    this.hypernetwork = hypernetwork;
  }

  /**
   * Returns predicted complexes for a given undirected graph of proteins.
   *
   * @param g the graph
   * @return the predicted complexes
   */
  public Collection<Complex> transform(UndirectedGraph<Protein, Interaction> g) {
    try {
      Process p = Runtime.getRuntime().exec(command);
      BufferedOutputStream outputStream = new BufferedOutputStream(p.getOutputStream());
      BufferedInputStream inputStream = new BufferedInputStream(p.getInputStream());

      for (Interaction i : g.getEdges()) {
        String out = i.first() + " pp " + i.second();
        outputStream.write(out.getBytes());
      }
      outputStream.close();
      p.waitFor();

      if (!hypernetwork.getProteins().hasIndex()) {
        hypernetwork.getProteins().buildIndex();
      }

      Collection<Complex> complexes = new ArrayList<Complex>();
      Scanner s = new Scanner(inputStream);
      Pattern rowPattern = Pattern.compile(".*?\\n");
      Pattern proteinPattern = Pattern.compile(".*?\\s");
      while (s.hasNext(rowPattern)) {
        Complex c = new Complex();
        while (s.hasNext(proteinPattern)) {
          c.add(hypernetwork.getProteins().getProteinById(s.next(proteinPattern).trim()));
        }
        complexes.add(c);
      }
      inputStream.close();

      return complexes;

    } catch (InterruptedException ex) {
      Logger.getLogger(ComplexPredictionCommand.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ComplexPredictionCommand.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
