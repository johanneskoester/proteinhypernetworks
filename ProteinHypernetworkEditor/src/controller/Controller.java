/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */
package controller;

import hypernetwork.constraints.Competition;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.TableModelEvent;
import proteinHypernetwork.ProteinHypernetwork;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.event.TableModelListener;
import javax.xml.stream.XMLStreamException;
import proteinHypernetwork.constraints.Constraint;
import proteinHypernetwork.constraints.Constraints;
import proteinHypernetwork.decoder.Decoder;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;
import proteinHypernetwork.decoder.PSIMI25Decoder;
import proteinHypernetwork.decoder.SifDecoder;
import proteinHypernetwork.encoder.HypernetworkMLEncoder;
import proteinHypernetwork.interactions.Interaction;
import proteinHypernetwork.interactions.filters.FilterInteractionsByProtein;
import proteinHypernetwork.proteins.Protein;
import proteinhypernetworkeditor.ProteinHypernetworkEditorApp;
import util.OneColumnTable;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class Controller {

  private static Controller instance = new Controller();

  public static Controller getInstance() {
    return instance;
  }
  private ProteinHypernetwork hypernetwork = new ProteinHypernetwork();
  private OneColumnTable proteinTable = new OneColumnTable(hypernetwork.getProteins());
  private OneColumnTable interactionTable = new OneColumnTable(hypernetwork.getInteractions());
  private OneColumnTable constraintTable = new OneColumnTable(hypernetwork.getConstraints());
  private File loaded;
  private PropertyChangeSupport propChange = new PropertyChangeSupport(this);

  public Controller() {
    TableModelListener changeListener = new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        propChange.firePropertyChange("hypernetwork", null, null);
      }
    };
    proteinTable.addTableModelListener(changeListener);
    interactionTable.addTableModelListener(changeListener);
    constraintTable.addTableModelListener(changeListener);
  }

  public ProteinHypernetwork getHypernetwork() {
    return hypernetwork;
  }

  public void setHypernetwork(ProteinHypernetwork h) {
    hypernetwork = h;
  }

  public OneColumnTable getProteinTable() {
    return proteinTable;
  }

  public OneColumnTable getInteractionTable() {
    return interactionTable;
  }

  public OneColumnTable getConstraintTable() {
    return constraintTable;
  }

  public void addProtein() {
    Protein p = new Protein();
    p.setId("Protein" + hypernetwork.getProteins().size());
    hypernetwork.getProteins().add(p);
    proteinTable.addRow(p);
  }

  public void removeProtein(int row) {
    Protein p = (Protein) proteinTable.getValueAt(row, 0);
    proteinTable.removeRow(row);
    hypernetwork.getProteins().remove(p);
  }

  public void removeProteins(int[] rows) {
    Set<Protein> toRemove = collectRows(proteinTable, rows);
    hypernetwork.getProteins().removeAll(toRemove);
    updateTables();
  }

  public void addInteraction() {
    Interaction i = new Interaction();
    hypernetwork.getInteractions().add(i);
    interactionTable.addRow(i);
  }

  public void removeInteraction(int row) {
    Interaction i = (Interaction) interactionTable.getValueAt(row, 0);
    interactionTable.removeRow(row);
    hypernetwork.getInteractions().remove(i);
  }

  public void removeInteractions(int[] rows) {
    hypernetwork.getInteractions().removeAll(collectRows(interactionTable, rows));
    updateTables();
  }

  public void addConstraint() {
    addConstraint(new Constraint());
  }

  public void addConstraint(Constraint c) {
    hypernetwork.getConstraints().add(c);
    constraintTable.addRow(c);
  }

  public void removeConstraint(int row) {
    Constraint c = (Constraint) constraintTable.getValueAt(row, 0);
    constraintTable.removeRow(row);
    hypernetwork.getConstraints().remove(c);
  }

  public void removeConstraints(int[] rows) {
    hypernetwork.getConstraints().removeAll(collectRows(constraintTable, rows));
    updateTables();
  }

  public boolean isLoadedFromFile() {
    return loaded != null;
  }

  public void save() throws Exception {
    if (loaded != null) {
      saveas(loaded);
    }
  }

  public void saveas(File file) throws XMLStreamException, FileNotFoundException {
    HypernetworkMLEncoder encoder = new HypernetworkMLEncoder();
    encoder.encode(hypernetwork, file);

    loaded = file;
  }

  public void loadFrom(File file) throws XMLStreamException, FileNotFoundException {
    HypernetworkMLDecoder decoder = new HypernetworkMLDecoder();
    hypernetwork = new ProteinHypernetwork();
    decoder.decode(file, hypernetwork);

    updateTables();

    loaded = file;
  }

  public void importFromSif(File file) throws Exception {
    decodeFrom(file, new SifDecoder());
  }

  public void importFromPSIMI25(File file) throws Exception {
    decodeFrom(file, new PSIMI25Decoder());
  }

  public void importConstraintsFromCsv(File file) throws FileNotFoundException {
    FilterInteractionsByProtein fp = new FilterInteractionsByProtein();
    Scanner s = new Scanner(file);

    if(!hypernetwork.getProteins().hasIndex())
      hypernetwork.getProteins().buildIndex();

    String prot = "[^,&&\\S]*";
    Pattern p = Pattern.compile(prot, Pattern.MULTILINE);
    int count = 0;
    while (s.hasNext()) {
      String l = s.next();
      System.out.println("Constraint " + ++count);

      Scanner cs = new Scanner(l);
      cs.useDelimiter(",");
      Protein host = hypernetwork.getProteins().getProteinById(trimQuotes(cs.next()));
      Protein meip1 = hypernetwork.getProteins().getProteinById(trimQuotes(cs.next()));
      Protein meip2 = hypernetwork.getProteins().getProteinById(trimQuotes(cs.next()));



      Collection<Interaction> is = fp.filter(hypernetwork.getInteractions(), host);
      Collection<Interaction> meis1 = fp.filter(is, meip1);
      Collection<Interaction> meis2 = fp.filter(is, meip2);

      if (host == meip1) {
        Iterator<Interaction> ite = meis1.iterator();
        while (ite.hasNext()) {
          if (!ite.next().isSelfInteraction()) {
            ite.remove();
          }
        }
      }

      if (host == meip2) {
        Iterator<Interaction> ite = meis2.iterator();
        while (ite.hasNext()) {
          if (!ite.next().isSelfInteraction()) {
            ite.remove();
          }
        }
      }

      int added = 0;
      for (Interaction mei1 : meis1) {
        for (Interaction mei2 : meis2) {
          // may be that same interaction appears twice if it contains both host and a mei
          if (!mei1.equals(mei2)) {
            for (Constraint c : new Competition(mei1, mei2)) {
              hypernetwork.getConstraints().add(c);
            }
            added++;
          }
        }
      }

      if (added < 1) {
        System.out.println("not added constraint:" + l);
      }


    }

    System.out.println(hypernetwork.getConstraints().size());

    updateTables();
  }

  public void removeRandomConstraints(int step, int num) throws XMLStreamException, FileNotFoundException {
    File original = loaded;
    DecimalFormat fPercent = new DecimalFormat("000");
    fPercent.setDecimalSeparatorAlwaysShown(false);
    DecimalFormat fNum = new DecimalFormat("0000");
    fNum.setDecimalSeparatorAlwaysShown(false);
    int percent = step;
    while (percent < 100) {
      String sPercent = fPercent.format(percent);
      for (int n = 0; n < num; n++) {
        removeRandomConstraints(percent);
        String path = (original.getParent() != null) ? original.getParent() + "/" : "";
        saveas(new File( path
                + original.getName().substring(0, original.getName().lastIndexOf(".hml"))
                + "_random_" + sPercent + "_" + fNum.format(n) + ".hml"));
        loadFrom(original);
      }
      percent += step;
    }
  }

  public void removeRandomConstraints(int percent) {
    Constraints constraints = hypernetwork.getConstraints();
    Random r = new Random();
    int toRemove = (int)(((100 - percent) / 100.0f) * (constraints.size() / 2));
    System.out.println(toRemove);
    System.out.println(constraints.size());

    while (toRemove > 0) {
      int i = r.nextInt(constraints.size());
      int j = constraints.getInverseIndex(constraints.get(i));
      if (i > j) {
        int t = i;
        i = j;
        j = t;
      }
      constraints.remove(j);
      constraints.remove(i);
      toRemove--;
    }
    updateTables();
  }

  private String trimQuotes(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      return value.substring(1, value.length() - 1);
    }
    return value;
  }

  private void decodeFrom(File file, Decoder decoder) throws Exception {
    decoder.decode(file, hypernetwork);
    updateTables();

    loaded = null;
  }

  private void updateTables() {
    proteinTable.setData(hypernetwork.getProteins());
    interactionTable.setData(hypernetwork.getInteractions());
    constraintTable.setData(hypernetwork.getConstraints());

    proteinTable.fireTableChanged();
    interactionTable.fireTableChanged();
    constraintTable.fireTableChanged();
  }

  private Set collectRows(OneColumnTable table, int[] rows) {
    Set collect = new HashSet();
    for (int row : rows) {
      collect.add(table.getValueAt(row, 0));
    }
    return collect;
  }

  public void addPropertyChangeListener(PropertyChangeListener p) {
    propChange.addPropertyChangeListener(p);
  }
}
