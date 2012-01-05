/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package proteinhypernetwork;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.complexes.lcma.LCMAComplexPrediction;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarityOutputStream;
import logicProteinHypernetwork.analysis.pis.PIS;
import org.jdesktop.application.Application;
import org.jdesktop.application.TaskEvent;
import org.jdesktop.application.TaskListener;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;
import proteinHypernetwork.proteins.Protein;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Controller {

  public static String COMPLEXESPREDICTED = "complexespredicted";
  public static String MASTERSWITCHESPREDICTED = "masterswitchespredicted";
  public static String FUNCTIONALSIMILARITIESPREDICTED = "functionalsimilaritiespredicted";
  public static String LOADED = "loaded";
  private static Controller controller = new Controller();

  public static Controller getInstance() {
    return controller;
  }
  private ProteinHypernetwork hypernetwork;
  private LogicProteinHypernetwork logicHypernetwork;
  private Task currentTask;
  private PropertyChangeSupport propChange = new PropertyChangeSupport(this);
  private File output;
  private boolean saveMasterSwitchComplexes = false;

  public void setOutput(File output) {
    this.output = output;
  }

  public LogicProteinHypernetwork getLogicHypernetwork() {
    return logicHypernetwork;
  }

  public void setMasterSwitchesDoSynthetic(boolean synthetic) {
    logicHypernetwork.setPISDoSynthetic(synthetic);
  }

  public void setMasterSwitchesDoInteractions(boolean interactions) {
    logicHypernetwork.setPISDoInteractions(interactions);
  }

  public void loadFrom(final File file) throws Exception {
    HypernetworkMLDecoder decoder = new HypernetworkMLDecoder();
    hypernetwork = new ProteinHypernetwork();

    decoder.decode(file, hypernetwork);
    logicHypernetwork = new LogicProteinHypernetwork(hypernetwork, ((ProteinHypernetworkApp) Application.getInstance()).getThreadCount());

    propChange.firePropertyChange(LOADED, null, null);
  }

  public Collection<NetworkEntity> getEntities() {
    Collection<NetworkEntity> entities = new ArrayList<NetworkEntity>(hypernetwork.getProteins());
    entities.addAll(hypernetwork.getInteractions());
    return entities;
  }

  public Collection<NetworkEntity> getPerturbedEntities() {
    return logicHypernetwork.getPerturbations();
  }

  public void predictComplexes() throws InstantiationException {
    LCMAComplexPrediction.setMergeSimilarityThreshold(ProteinHypernetworkApp.getApplication().getMergeSimilarityThreshold());
    
    LogicProteinHypernetwork.setProteinComplexPrediction(ProteinHypernetworkApp.getApplication().getComplexPrediction());
    
    logicHypernetwork.predictComplexes();
    for (Complex c : logicHypernetwork.getComplexes()) {
      Collections.sort(c);
    }
    propChange.firePropertyChange(COMPLEXESPREDICTED, null, null);
  }

  public void predictComplexesBackground() {
    executeTask(new logicProteinHypernetworkTask(true) {

      @Override
      protected Object doInBackground() throws Exception {
        predictComplexes();
        return null;
      }
    });
  }

  public void predictMasterSwitches() {
    logicHypernetwork.predictPIS();
    propChange.firePropertyChange(MASTERSWITCHESPREDICTED, null, null);
  }

  public void predictMasterSwitchesBackground() {
    executeTask(new logicProteinHypernetworkTask(true) {

      @Override
      protected Object doInBackground() throws Exception {
        predictMasterSwitches();
        return null;
      }
    });
  }

  public void predictFunctionalSimilarities() throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(output);
    FunctionalSimilarityOutputStream os = new FunctionalSimilarityOutputStream(fos);
    logicHypernetwork.predictFunctionalSimilarities(os);
    fos.close();
    propChange.firePropertyChange(FUNCTIONALSIMILARITIESPREDICTED, null, null);
  }

  public void predictFunctionalSimilaritiesBackground() {
    executeTask(new logicProteinHypernetworkTask(true) {

      @Override
      protected Object doInBackground() throws Exception {
        predictFunctionalSimilarities();
        return null;
      }
    });
  }

  public void perturbation(final NetworkEntity e) {
    logicHypernetwork.perturbation(e);
  }

  public void undoPerturbation(final NetworkEntity e) {
    logicHypernetwork.undoPerturbation(e);
  }

  public Collection<Complex> getComplexes() {
    return logicHypernetwork.getComplexes();
  }

  public List<? extends PIS> getMasterSwitches() {
    return logicHypernetwork.getPIS();
  }

  public void exportComplexesToTsv() throws IOException {
    List<Complex> complexes = new ArrayList<Complex>(getComplexes());
    Collections.sort(complexes, Collections.reverseOrder());
    exportComplexesToTsv(output, complexes);
  }

  public void exportMasterSwitchesToTsv() throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(output));

    List<PIS> masterSwitches = new ArrayList<PIS>(getMasterSwitches());
    Collections.sort(masterSwitches, Collections.reverseOrder());
    for (PIS m : masterSwitches) {
      for(NetworkEntity e : m)
        out.write(e.toString() + "\t");
      out.write(m.getScore() + "\n");
    }

    out.close();
  }

  public void addPropertyChangeListener(PropertyChangeListener p) {
    propChange.addPropertyChangeListener(p);
  }

  private void exportComplexesToTsv(File file, Collection<Complex> complexes) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(file));

    for (Complex c : complexes) {
      Iterator<Protein> ps = c.iterator();
      while (ps.hasNext()) {
        out.write(ps.next().toString());
        if (ps.hasNext()) {
          out.write("\t");
        }
      }
      out.newLine();
    }
    out.close();
  }

  private void executeTask(Task task) {
    if (currentTask != null) {
      currentTask.cancel(true);
    }

    currentTask = task;
    currentTask.execute();

    currentTask = null;
  }

  abstract class logicProteinHypernetworkTask extends Task {

    public logicProteinHypernetworkTask(final boolean fireEventOnSucceed) {
      this.addTaskListener(new TaskListener() {

        @Override
        public void doInBackground(TaskEvent event) {
          logicHypernetwork.getProgressBean().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
              int p = (Integer) evt.getNewValue();
              if (p > -1 && p < 101) {
                setProgress(p);
              }
            }
          });
        }

        @Override
        public void process(TaskEvent event) {
        }

        @Override
        public void succeeded(TaskEvent event) {
          if (fireEventOnSucceed) {
            propChange.firePropertyChange("hypernetwork", null, null);
          }
        }

        @Override
        public void failed(TaskEvent event) {
        }

        @Override
        public void cancelled(TaskEvent event) {
        }

        @Override
        public void interrupted(TaskEvent event) {
        }

        @Override
        public void finished(TaskEvent event) {
        }
      });
    }
  }

  private File getOutputDirectory() {
    if (output.isDirectory()) {
      return output;
    } else {
      return output.getParentFile();
    }
  }
}
