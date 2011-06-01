/*
 * ProteinHypernetworkEditorView.java
 */
package proteinhypernetworkeditor;

import controller.Controller;
import java.io.FileNotFoundException;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.application.Application.ExitListener;
import proteinHypernetwork.util.ProteinHypernetworkFileFilter;

/**
 * The application's main frame.
 */
public class ProteinHypernetworkEditorView extends FrameView {

  public ProteinHypernetworkEditorView(SingleFrameApplication app) {
    super(app);

    initComponents();

    ToolTipManager.sharedInstance().setInitialDelay(0);

    // status bar initialization - message timeout, idle icon and busy animation, etc
    ResourceMap resourceMap = getResourceMap();
    int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
    messageTimer = new Timer(messageTimeout, new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        statusMessageLabel.setText("");
      }
    });
    messageTimer.setRepeats(false);
    progressBar.setVisible(false);

    // connecting action tasks to status bar via TaskMonitor
    TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
    taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("started".equals(propertyName)) {
          progressBar.setVisible(true);
          progressBar.setIndeterminate(true);
        } else if ("done".equals(propertyName)) {
          progressBar.setVisible(false);
          progressBar.setValue(0);
        } else if ("message".equals(propertyName)) {
          String text = (String) (evt.getNewValue());
          statusMessageLabel.setText((text == null) ? "" : text);
          messageTimer.restart();
        } else if ("progress".equals(propertyName)) {
          int value = (Integer) (evt.getNewValue());
          progressBar.setVisible(true);
          progressBar.setIndeterminate(false);
          progressBar.setValue(value);
        }
      }
    });

    ProteinHypernetworkEditorApp.getApplication().addExitListener(new ExitListener() {

      @Override
      public boolean canExit(EventObject event) {
        if (Controller.getInstance().getHypernetwork().isEmpty()) {
          return true;
        }

        while (true) {
          int answer = JOptionPane.showConfirmDialog(getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("save_on_close"));

          if (answer == JOptionPane.OK_OPTION) {
            boolean ok;
            try {
              ok = save();
            } catch (Exception ex) {
              ok = false;
            }
            if (ok) {
              return true;
            }
          }
          if (answer == JOptionPane.CANCEL_OPTION) {
            return false;
          }
          if (answer == JOptionPane.NO_OPTION) {
            return true;
          }
        }
      }

      @Override
      public void willExit(EventObject event) {
      }
    });

    /** @todo find a proper way to give an error message here */
    String open = ProteinHypernetworkEditorApp.open;
    if(open != null) {
      try {
        Controller.getInstance().loadFrom(new File(open));
      } catch (XMLStreamException ex) {
        Logger.getLogger(ProteinHypernetworkEditorView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(ProteinHypernetworkEditorView.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Action
  public void showAboutBox() {
    if (aboutBox == null) {
      JFrame mainFrame = ProteinHypernetworkEditorApp.getApplication().getMainFrame();
      aboutBox = new ProteinHypernetworkEditorAboutBox(mainFrame);
      aboutBox.setLocationRelativeTo(mainFrame);
    }
    ProteinHypernetworkEditorApp.getApplication().show(aboutBox);
  }

  public boolean save() throws Exception {
    if (Controller.getInstance().isLoadedFromFile()) {
      Controller.getInstance().save();
      return true;
    } else {
      return saveas();
    }
  }

  public boolean saveas() {
    FileChooser.setFileFilter(new ProteinHypernetworkFileFilter());
    File f = FileChooser.saveFile();
    if (f != null) {
      try {
        Controller.getInstance().saveas(f);
        return true;
      } catch (Exception ex) {
        System.err.println(ex);
        JOptionPane.showMessageDialog(this.getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("filenotfound"));
      }
    }
    return false;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    mainPanel = new javax.swing.JPanel();
    tasksView1 = new proteinhypernetworkeditor.TasksView();
    sidebar1 = new sidebar.Sidebar();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    editProteins = new javax.swing.JToggleButton();
    editInteractions = new javax.swing.JToggleButton();
    jLabel3 = new javax.swing.JLabel();
    editConstraints = new javax.swing.JToggleButton();
    visualizeInteractions = new javax.swing.JToggleButton();
    visualizeConstraints = new javax.swing.JToggleButton();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    load = new javax.swing.JMenuItem();
    save = new javax.swing.JMenuItem();
    saveas = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    importFrom = new javax.swing.JMenu();
    importFromSif = new javax.swing.JMenuItem();
    importFromMIPS = new javax.swing.JMenuItem();
    importConstraints = new javax.swing.JMenu();
    importConstraintsFromCsv = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    statusMessageLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    fileChooser = new javax.swing.JFileChooser();
    taskButtons = new javax.swing.ButtonGroup();

    mainPanel.setName("mainPanel"); // NOI18N
    mainPanel.setLayout(new java.awt.GridBagLayout());

    tasksView1.setName("tasksView1"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
    mainPanel.add(tasksView1, gridBagConstraints);

    sidebar1.setName("sidebar1"); // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getResourceMap(ProteinHypernetworkEditorView.class);
    jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
    jLabel1.setName("jLabel1"); // NOI18N

    jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
    jLabel2.setName("jLabel2"); // NOI18N

    taskButtons.add(editProteins);
    editProteins.setSelected(true);
    editProteins.setText(resourceMap.getString("editProteins.text")); // NOI18N
    editProteins.setToolTipText(resourceMap.getString("editProteins.toolTipText")); // NOI18N
    editProteins.setName("editProteins"); // NOI18N
    editProteins.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editProteinsActionPerformed(evt);
      }
    });

    taskButtons.add(editInteractions);
    editInteractions.setText(resourceMap.getString("editInteractions.text")); // NOI18N
    editInteractions.setToolTipText(resourceMap.getString("editInteractions.toolTipText")); // NOI18N
    editInteractions.setName("editInteractions"); // NOI18N
    editInteractions.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editInteractionsActionPerformed(evt);
      }
    });

    jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
    jLabel3.setName("jLabel3"); // NOI18N

    taskButtons.add(editConstraints);
    editConstraints.setText(resourceMap.getString("editConstraints.text")); // NOI18N
    editConstraints.setToolTipText(resourceMap.getString("editConstraints.toolTipText")); // NOI18N
    editConstraints.setName("editConstraints"); // NOI18N
    editConstraints.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editConstraintsActionPerformed(evt);
      }
    });

    taskButtons.add(visualizeInteractions);
    visualizeInteractions.setText(resourceMap.getString("visualizeInteractions.text")); // NOI18N
    visualizeInteractions.setToolTipText(resourceMap.getString("visualizeInteractions.toolTipText")); // NOI18N
    visualizeInteractions.setName("visualizeInteractions"); // NOI18N
    visualizeInteractions.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        visualizeInteractionsActionPerformed(evt);
      }
    });

    taskButtons.add(visualizeConstraints);
    visualizeConstraints.setText(resourceMap.getString("visualizeConstraints.text")); // NOI18N
    visualizeConstraints.setToolTipText(resourceMap.getString("visualizeConstraints.toolTipText")); // NOI18N
    visualizeConstraints.setName("visualizeConstraints"); // NOI18N
    visualizeConstraints.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        visualizeConstraintsActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout sidebar1Layout = new org.jdesktop.layout.GroupLayout(sidebar1);
    sidebar1.setLayout(sidebar1Layout);
    sidebar1Layout.setHorizontalGroup(
      sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(sidebar1Layout.createSequentialGroup()
        .addContainerGap()
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(editProteins)
          .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(sidebar1Layout.createSequentialGroup()
            .add(editInteractions)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(visualizeInteractions))
          .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(sidebar1Layout.createSequentialGroup()
            .add(editConstraints)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(visualizeConstraints)))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    sidebar1Layout.setVerticalGroup(
      sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(sidebar1Layout.createSequentialGroup()
        .addContainerGap()
        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .add(6, 6, 6)
        .add(editProteins)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(editInteractions)
          .add(visualizeInteractions))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(editConstraints)
          .add(visualizeConstraints))
        .addContainerGap(305, Short.MAX_VALUE))
    );

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    mainPanel.add(sidebar1, gridBagConstraints);

    menuBar.setName("menuBar"); // NOI18N

    fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
    fileMenu.setToolTipText(resourceMap.getString("fileMenu.toolTipText")); // NOI18N
    fileMenu.setName("fileMenu"); // NOI18N

    load.setText(resourceMap.getString("load.text")); // NOI18N
    load.setName("load"); // NOI18N
    load.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        loadActionPerformed(evt);
      }
    });
    fileMenu.add(load);

    save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    save.setText(resourceMap.getString("save.text")); // NOI18N
    save.setName("save"); // NOI18N
    save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveActionPerformed(evt);
      }
    });
    fileMenu.add(save);

    saveas.setText(resourceMap.getString("saveas.text")); // NOI18N
    saveas.setName("saveas"); // NOI18N
    saveas.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveasActionPerformed(evt);
      }
    });
    fileMenu.add(saveas);

    jSeparator2.setName("jSeparator2"); // NOI18N
    fileMenu.add(jSeparator2);

    importFrom.setText(resourceMap.getString("importFrom.text")); // NOI18N
    importFrom.setName("importFrom"); // NOI18N

    importFromSif.setText(resourceMap.getString("importFromSif.text")); // NOI18N
    importFromSif.setName("importFromSif"); // NOI18N
    importFromSif.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importFromSifActionPerformed(evt);
      }
    });
    importFrom.add(importFromSif);

    importFromMIPS.setText(resourceMap.getString("importFromMIPS.text")); // NOI18N
    importFromMIPS.setName("importFromMIPS"); // NOI18N
    importFromMIPS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importFromMIPSActionPerformed(evt);
      }
    });
    importFrom.add(importFromMIPS);

    importConstraints.setText(resourceMap.getString("importConstraints.text")); // NOI18N
    importConstraints.setName("importConstraints"); // NOI18N

    importConstraintsFromCsv.setText(resourceMap.getString("importConstraintsFromCsv.text")); // NOI18N
    importConstraintsFromCsv.setName("importConstraintsFromCsv"); // NOI18N
    importConstraintsFromCsv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importConstraintsFromCsvActionPerformed(evt);
      }
    });
    importConstraints.add(importConstraintsFromCsv);

    importFrom.add(importConstraints);

    fileMenu.add(importFrom);

    jSeparator1.setName("jSeparator1"); // NOI18N
    fileMenu.add(jSeparator1);

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(proteinhypernetworkeditor.ProteinHypernetworkEditorApp.class).getContext().getActionMap(ProteinHypernetworkEditorView.class, this);
    exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
    exitMenuItem.setName("exitMenuItem"); // NOI18N
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
    helpMenu.setName("helpMenu"); // NOI18N

    aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
    aboutMenuItem.setName("aboutMenuItem"); // NOI18N
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    statusPanel.setName("statusPanel"); // NOI18N
    statusPanel.setVisible(false);
    statusPanel.setLayout(new java.awt.GridBagLayout());

    statusMessageLabel.setName("statusMessageLabel"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    statusPanel.add(statusMessageLabel, gridBagConstraints);

    progressBar.setName("progressBar"); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    statusPanel.add(progressBar, gridBagConstraints);

    fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
    fileChooser.setName("fileChooser"); // NOI18N

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

    private void saveasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveasActionPerformed
      saveas();
    }//GEN-LAST:event_saveasActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
      FileChooser.setFileFilter(new ProteinHypernetworkFileFilter());
      File f = FileChooser.openFile();
      if (f != null) {
        try {
          Controller.getInstance().loadFrom(f);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this.getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("filenotfound"));
        }
      }
    }//GEN-LAST:event_loadActionPerformed

    private void importFromSifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFromSifActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("Cytoscape Simple Interaction Format (.sif)", "sif"));
      File f = FileChooser.openFile();
      if (f != null) {
        try {
          Controller.getInstance().importFromSif(f);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this.getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("filenotfound"));
        }
      }
    }//GEN-LAST:event_importFromSifActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
      try {
        save();
      } catch (Exception ex) {
        System.err.println(ex);
      }
    }//GEN-LAST:event_saveActionPerformed

    private void importFromMIPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFromMIPSActionPerformed
      FileChooser.setFileFilter(new FileFilter() {

      @Override
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith("psi25.xml");
      }

      @Override
      public String getDescription() {
        return "PSI MIPS 2.5 (.psi25.xml)";
      }
    });
      FileChooser.setFileFilter(new FileNameExtensionFilter("PSI MIPS 2.5", "psi25.xml"));
      File f = FileChooser.openFile();
      if (f != null) {
        try {
          Controller.getInstance().importFromPSIMI25(f);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this.getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("filenotfound"));
        }
      }
    }//GEN-LAST:event_importFromMIPSActionPerformed

    private void importConstraintsFromCsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importConstraintsFromCsvActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("CSV containing mutual exclusive interactions: Host, Binding parter 1, Binding partner 2 (.csv)", "csv"));
      File f = FileChooser.openFile();
      if (f != null) {
        try {
          Controller.getInstance().importConstraintsFromCsv(f);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this.getComponent(), java.util.ResourceBundle.getBundle("proteinhypernetworkeditor/resources/ProteinHypernetworkEditorView").getString("filenotfound"));
        }
      }
    }//GEN-LAST:event_importConstraintsFromCsvActionPerformed

    private void editProteinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProteinsActionPerformed
      if(editProteins.isSelected())
        tasksView1.activateProteinManager();
    }//GEN-LAST:event_editProteinsActionPerformed

    private void editInteractionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editInteractionsActionPerformed
      if(editInteractions.isSelected())
        tasksView1.activateInteractionManager();
    }//GEN-LAST:event_editInteractionsActionPerformed

    private void editConstraintsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editConstraintsActionPerformed
      if(editConstraints.isSelected())
        tasksView1.activateConstraintManager();
    }//GEN-LAST:event_editConstraintsActionPerformed

    private void visualizeInteractionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeInteractionsActionPerformed
      if(visualizeInteractions.isSelected())
        tasksView1.activateNetworkVisualization();
    }//GEN-LAST:event_visualizeInteractionsActionPerformed

    private void visualizeConstraintsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeConstraintsActionPerformed
      if(visualizeConstraints.isSelected())
        tasksView1.activateConstraintsVisualization();
    }//GEN-LAST:event_visualizeConstraintsActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToggleButton editConstraints;
  private javax.swing.JToggleButton editInteractions;
  private javax.swing.JToggleButton editProteins;
  private javax.swing.JFileChooser fileChooser;
  private javax.swing.JMenu importConstraints;
  private javax.swing.JMenuItem importConstraintsFromCsv;
  private javax.swing.JMenu importFrom;
  private javax.swing.JMenuItem importFromMIPS;
  private javax.swing.JMenuItem importFromSif;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JMenuItem load;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JMenuItem save;
  private javax.swing.JMenuItem saveas;
  private sidebar.Sidebar sidebar1;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  private javax.swing.ButtonGroup taskButtons;
  private proteinhypernetworkeditor.TasksView tasksView1;
  private javax.swing.JToggleButton visualizeConstraints;
  private javax.swing.JToggleButton visualizeInteractions;
  // End of variables declaration//GEN-END:variables
  private final Timer messageTimer;
  private JDialog aboutBox;
}
