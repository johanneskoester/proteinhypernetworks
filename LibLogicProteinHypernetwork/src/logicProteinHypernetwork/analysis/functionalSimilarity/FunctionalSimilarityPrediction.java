/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package logicProteinHypernetwork.analysis.functionalSimilarity;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.Processor;
import logicProteinHypernetwork.analysis.pis.SinglePIS;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.proteins.Protein;
import util.MyCollectionUtils;
import util.ProgressBean;

/**
 * Predict functional similarity.
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class FunctionalSimilarityPrediction extends Processor {

  private List<SinglePIS> pis;
  private ProteinHypernetwork hypernetwork;
  private FunctionalSimilarityOutputStream outputStream;

  public FunctionalSimilarityPrediction(ProteinHypernetwork hypernetwork, List<SinglePIS> pis) {
    this.pis = pis;
    this.hypernetwork = hypernetwork;
  }


  public void setOutputStream(FunctionalSimilarityOutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public void process() {
    try {
      System.out.println("Predicting functional similarities...");
      calculateScores(true, outputStream);
      System.out.println("Finished predicting functional similarities...");
    } catch (IOException ex) {
      System.err.println("Error predicting functional similarities...");
    }
  }

  private void calculateScores(boolean protein, FunctionalSimilarityOutputStream os) throws IOException {
    int entities = hypernetwork.getInteractions().size();
    if(protein) {
      entities = hypernetwork.getProteins().size();
    }
    int max = (int)Math.pow(entities, 2) / 2;
    
    int k = 0;
    for (int i = 0; i < pis.size(); i++) {
      SinglePIS m1 = pis.get(i);
      
      if ((protein && m1.getEntity() instanceof Protein)
              || (!protein && m1.getEntity() instanceof Interaction)) {
        for (int j = i + 1; j < pis.size(); j++) {
          SinglePIS m2 = pis.get(j);
          
          if ((protein && m2.getEntity() instanceof Protein)
                  || (!protein && m2.getEntity() instanceof Interaction)) {

            int intersection = MyCollectionUtils.intersectionSize(m1.getAffected(), m2.getAffected());
            float similarity = intersection
                    / ((float) m1.getAffected().size()
                    + m2.getAffected().size() - intersection);

            FunctionalSimilarity fs =
                    new FunctionalSimilarity(m1.getEntity(), m2.getEntity(), similarity);
            os.write(fs);

            System.out.println(k);
            
            progressBean.setProgress(++k, max);
          }
        }
      }
    }
  }
}
