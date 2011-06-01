/*
 * ProteinHypernetworkView.java
 */
package proteinhypernetwork;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import proteinHypernetwork.util.ProteinHypernetworkFileFilter;

/**
 * The application's main frame.
 */
public class ProteinHypernetworkView extends FrameView {

  public ProteinHypernetworkView(SingleFrameApplication app) {
    super(app);

    initComponents();
    ToolTipManager.sharedInstance().setInitialDelay(0);

    // status bar initialization - message timeout, idle icon and busy animation, etc
    ResourceMap resourceMap = getResourceMap();
    int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
    messageTimer = new Timer(messageTimeout, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        statusMessageLabel.setText("");
      }
    });
    messageTimer.setRepeats(false);
    progressBar.setVisible(false);

    // connecting action tasks to status bar via TaskMonitor
    TaskMonitor taskMonitor = getApplication().getContext().getTaskMonitor();
    taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

      @Override
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

    Controller.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Controller.COMPLEXESPREDICTED)) {
          predictComplexes.setEnabled(true);
          predictMasterSwitches.setEnabled(true);
          syntheticPIS.setEnabled(true);
          interactionsPIS.setEnabled(true);
          visualizeComplexes.setEnabled(true);
          listComplexes.setEnabled(true);
          visualizeComplexes.setEnabled(true);
          listComplexes.setSelected(true);
          listComplexesActionPerformed(null);
          exportTsv.setEnabled(true);
          managePerturbations.setEnabled(true);
        }
        if (evt.getPropertyName().equals(Controller.MASTERSWITCHESPREDICTED)) {
          predictMasterSwitches.setEnabled(true);
          syntheticPIS.setEnabled(true);
          interactionsPIS.setEnabled(true);
          predictComplexes.setEnabled(true);
          //predictFunctionalSimilaritites.setEnabled(true);
          listMasterSwitches.setEnabled(true);
          if(!lastPISSynthetic)
            visualizeMasterSwitches.setEnabled(true);
          listMasterSwitches.setSelected(true);
          listMasterSwitchesActionPerformed(null);
          exportMasterSwitches.setEnabled(true);
          managePerturbations.setEnabled(true);
        }
        if (evt.getPropertyName().equals(Controller.FUNCTIONALSIMILARITIESPREDICTED)) {
          predictMasterSwitches.setEnabled(true);
          syntheticPIS.setEnabled(true);
          interactionsPIS.setEnabled(true);
          predictComplexes.setEnabled(true);
          //predictFunctionalSimilaritites.setEnabled(true);
        }
      }
    });

    tasksView1.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(TasksView.VISUALIZATIONACTIVE)) {
          boolean active = (Boolean) evt.getNewValue();
          exportVisualization.setEnabled(active);
          exportVisualizationToPNG.setEnabled(active);
        }
      }
    });
  }

  @Action
  public void showAboutBox() {
    if (aboutBox == null) {
      JFrame mainFrame = ProteinHypernetworkApp.getApplication().getMainFrame();
      aboutBox = new ProteinHypernetworkAboutBox(mainFrame);
      aboutBox.setLocationRelativeTo(mainFrame);
    }
    ProteinHypernetworkApp.getApplication().show(aboutBox);
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
    tasksView1 = new proteinhypernetwork.TasksView();
    sidebar1 = new sidebar.Sidebar();
    predictComplexes = new javax.swing.JButton();
    predictMasterSwitches = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    listComplexes = new javax.swing.JToggleButton();
    listMasterSwitches = new javax.swing.JToggleButton();
    jLabel3 = new javax.swing.JLabel();
    managePerturbations = new javax.swing.JToggleButton();
    visualizeComplexes = new javax.swing.JToggleButton();
    visualizeMasterSwitches = new javax.swing.JToggleButton();
    syntheticPIS = new javax.swing.JCheckBox();
    interactionsPIS = new javax.swing.JCheckBox();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    open = new javax.swing.JMenuItem();
    exportMenu = new javax.swing.JMenu();
    exportTsv = new javax.swing.JMenuItem();
    exportMasterSwitches = new javax.swing.JMenuItem();
    exportVisualization = new javax.swing.JMenuItem();
    exportVisualizationToPNG = new javax.swing.JMenuItem();
    options = new javax.swing.JMenuItem();
    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    statusMessageLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    jPanel1 = new javax.swing.JPanel();
    tasks = new javax.swing.ButtonGroup();

    mainPanel.setMinimumSize(new java.awt.Dimension(600, 400));
    mainPanel.setName("mainPanel"); // NOI18N
    mainPanel.setPreferredSize(new java.awt.Dimension(600, 400));
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

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getResourceMap(ProteinHypernetworkView.class);
    predictComplexes.setText(resourceMap.getString("predictComplexes.text")); // NOI18N
    predictComplexes.setToolTipText(resourceMap.getString("predictComplexes.toolTipText")); // NOI18N
    predictComplexes.setEnabled(false);
    predictComplexes.setFocusable(false);
    predictComplexes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    predictComplexes.setName("predictComplexes"); // NOI18N
    predictComplexes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    predictComplexes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        predictComplexesActionPerformed(evt);
      }
    });

    predictMasterSwitches.setText(resourceMap.getString("predictMasterSwitches.text")); // NOI18N
    predictMasterSwitches.setToolTipText(resourceMap.getString("predictMasterSwitches.toolTipText")); // NOI18N
    predictMasterSwitches.setEnabled(false);
    predictMasterSwitches.setFocusable(false);
    predictMasterSwitches.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    predictMasterSwitches.setName("predictMasterSwitches"); // NOI18N
    predictMasterSwitches.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    predictMasterSwitches.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        predictMasterSwitchesActionPerformed(evt);
      }
    });

    jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
    jLabel1.setName("jLabel1"); // NOI18N

    jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
    jLabel2.setName("jLabel2"); // NOI18N

    tasks.add(listComplexes);
    listComplexes.setSelected(true);
    listComplexes.setText(resourceMap.getString("listComplexes.text")); // NOI18N
    listComplexes.setToolTipText(resourceMap.getString("listComplexes.toolTipText")); // NOI18N
    listComplexes.setEnabled(false);
    listComplexes.setName("listComplexes"); // NOI18N
    listComplexes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        listComplexesActionPerformed(evt);
      }
    });

    tasks.add(listMasterSwitches);
    listMasterSwitches.setText(resourceMap.getString("listMasterSwitches.text")); // NOI18N
    listMasterSwitches.setToolTipText(resourceMap.getString("listMasterSwitches.toolTipText")); // NOI18N
    listMasterSwitches.setEnabled(false);
    listMasterSwitches.setName("listMasterSwitches"); // NOI18N
    listMasterSwitches.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        listMasterSwitchesActionPerformed(evt);
      }
    });

    jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
    jLabel3.setName("jLabel3"); // NOI18N

    tasks.add(managePerturbations);
    managePerturbations.setText(resourceMap.getString("managePerturbations.text")); // NOI18N
    managePerturbations.setToolTipText(resourceMap.getString("managePerturbations.toolTipText")); // NOI18N
    managePerturbations.setEnabled(false);
    managePerturbations.setName("managePerturbations"); // NOI18N
    managePerturbations.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        managePerturbationsActionPerformed(evt);
      }
    });

    tasks.add(visualizeComplexes);
    visualizeComplexes.setText(resourceMap.getString("visualizeComplexes.text")); // NOI18N
    visualizeComplexes.setToolTipText(resourceMap.getString("visualizeComplexes.toolTipText")); // NOI18N
    visualizeComplexes.setEnabled(false);
    visualizeComplexes.setName("visualizeComplexes"); // NOI18N
    visualizeComplexes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        visualizeComplexesActionPerformed(evt);
      }
    });

    tasks.add(visualizeMasterSwitches);
    visualizeMasterSwitches.setText(resourceMap.getString("visualizeMasterSwitches.text")); // NOI18N
    visualizeMasterSwitches.setToolTipText(resourceMap.getString("visualizeMasterSwitches.toolTipText")); // NOI18N
    visualizeMasterSwitches.setEnabled(false);
    visualizeMasterSwitches.setName("visualizeMasterSwitches"); // NOI18N
    visualizeMasterSwitches.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        visualizeMasterSwitchesActionPerformed(evt);
      }
    });

    syntheticPIS.setText(resourceMap.getString("syntheticPIS.text")); // NOI18N
    syntheticPIS.setToolTipText(resourceMap.getString("syntheticPIS.toolTipText")); // NOI18N
    syntheticPIS.setEnabled(false);
    syntheticPIS.setName("syntheticPIS"); // NOI18N

    interactionsPIS.setText(resourceMap.getString("interactionsPIS.text")); // NOI18N
    interactionsPIS.setToolTipText(resourceMap.getString("interactionsPIS.toolTipText")); // NOI18N
    interactionsPIS.setEnabled(false);
    interactionsPIS.setName("interactionsPIS"); // NOI18N

    org.jdesktop.layout.GroupLayout sidebar1Layout = new org.jdesktop.layout.GroupLayout(sidebar1);
    sidebar1.setLayout(sidebar1Layout);
    sidebar1Layout.setHorizontalGroup(
      sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(sidebar1Layout.createSequentialGroup()
        .addContainerGap()
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(sidebar1Layout.createSequentialGroup()
            .add(predictComplexes)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(listComplexes)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(visualizeComplexes))
          .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(sidebar1Layout.createSequentialGroup()
            .add(syntheticPIS)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(interactionsPIS))
          .add(sidebar1Layout.createSequentialGroup()
            .add(predictMasterSwitches)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(listMasterSwitches)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(visualizeMasterSwitches))
          .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(managePerturbations))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    sidebar1Layout.setVerticalGroup(
      sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, sidebar1Layout.createSequentialGroup()
        .addContainerGap()
        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
          .add(predictComplexes)
          .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(listComplexes)
            .add(visualizeComplexes)))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(syntheticPIS)
          .add(interactionsPIS))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(predictMasterSwitches)
          .add(sidebar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(listMasterSwitches)
            .add(visualizeMasterSwitches)))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(managePerturbations)
        .add(304, 304, 304))
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

    open.setText(resourceMap.getString("open.text")); // NOI18N
    open.setName("open"); // NOI18N
    open.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openActionPerformed(evt);
      }
    });
    fileMenu.add(open);

    exportMenu.setText(resourceMap.getString("exportMenu.text")); // NOI18N
    exportMenu.setName("exportMenu"); // NOI18N

    exportTsv.setText(resourceMap.getString("exportTsv.text")); // NOI18N
    exportTsv.setEnabled(false);
    exportTsv.setName("exportTsv"); // NOI18N
    exportTsv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportTsvActionPerformed(evt);
      }
    });
    exportMenu.add(exportTsv);

    exportMasterSwitches.setText(resourceMap.getString("exportMasterSwitches.text")); // NOI18N
    exportMasterSwitches.setEnabled(false);
    exportMasterSwitches.setName("exportMasterSwitches"); // NOI18N
    exportMasterSwitches.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportMasterSwitchesActionPerformed(evt);
      }
    });
    exportMenu.add(exportMasterSwitches);

    exportVisualization.setText(resourceMap.getString("exportVisualization.text")); // NOI18N
    exportVisualization.setEnabled(false);
    exportVisualization.setName("exportVisualization"); // NOI18N
    exportVisualization.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportVisualizationActionPerformed(evt);
      }
    });
    exportMenu.add(exportVisualization);

    exportVisualizationToPNG.setText(resourceMap.getString("exportVisualizationToPNG.text")); // NOI18N
    exportVisualizationToPNG.setEnabled(false);
    exportVisualizationToPNG.setName("exportVisualizationToPNG"); // NOI18N
    exportVisualizationToPNG.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportVisualizationToPNGActionPerformed(evt);
      }
    });
    exportMenu.add(exportVisualizationToPNG);

    fileMenu.add(exportMenu);

    options.setText(resourceMap.getString("options.text")); // NOI18N
    options.setName("options"); // NOI18N
    fileMenu.add(options);

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(proteinhypernetwork.ProteinHypernetworkApp.class).getContext().getActionMap(ProteinHypernetworkView.class, this);
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

    jPanel1.setName("jPanel1"); // NOI18N

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(0, 100, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(0, 100, Short.MAX_VALUE)
    );

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
      FileChooser.setFileFilter(new ProteinHypernetworkFileFilter());
      File f = FileChooser.openFile();

      if (f != null) {
        try {
          Controller.getInstance().loadFrom(f);
          predictComplexes.setEnabled(true);
          predictMasterSwitches.setEnabled(true);
          syntheticPIS.setEnabled(true);
          interactionsPIS.setEnabled(true);
          //predictFunctionalSimilaritites.setEnabled(false);
          listComplexes.setEnabled(false);
          listMasterSwitches.setEnabled(false);
          managePerturbations.setEnabled(true);
          visualizeComplexes.setEnabled(false);
          visualizeMasterSwitches.setEnabled(false);
          listComplexes.setSelected(true);
          tasksView1.activateComplexPredictionView();
          exportTsv.setEnabled(false);
          exportMasterSwitches.setEnabled(false);
          exportVisualization.setEnabled(false);
        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(this.getComponent(), "Error loading Protein Hypernetwork");
        }
      }
    }//GEN-LAST:event_openActionPerformed

    private void exportTsvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportTsvActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("Complexes in tab separated value file (.tsv)", "tsv"));
      File f = FileChooser.saveFile();

      if (f != null) {
        try {
          Controller.getInstance().setOutput(f);
          Controller.getInstance().exportComplexesToTsv();
        } catch (IOException ex) {
          System.err.println(ex);
          JOptionPane.showMessageDialog(this.getComponent(), "Error exporting to TSV");
        }
      }
    }//GEN-LAST:event_exportTsvActionPerformed

    private void exportMasterSwitchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMasterSwitchesActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("Master Switches in tab separated value file (.tsv)", "tsv"));
      File f = FileChooser.saveFile();

      if (f != null) {
        try {
          Controller.getInstance().setOutput(f);
          Controller.getInstance().exportMasterSwitchesToTsv();
        } catch (IOException ex) {
          System.err.println(ex);
          JOptionPane.showMessageDialog(this.getComponent(), "Error exporting to TSV");
        }
      }
    }//GEN-LAST:event_exportMasterSwitchesActionPerformed

    private void predictMasterSwitchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_predictMasterSwitchesActionPerformed
      predictMasterSwitches.setEnabled(false);
      predictComplexes.setEnabled(false);
      managePerturbations.setEnabled(false);
      syntheticPIS.setEnabled(false);
      interactionsPIS.setEnabled(false);
      //predictFunctionalSimilaritites.setEnabled(false);
      lastPISSynthetic = syntheticPIS.isSelected();
      Controller.getInstance().setMasterSwitchesDoSynthetic(syntheticPIS.isSelected());
      Controller.getInstance().setMasterSwitchesDoInteractions(interactionsPIS.isSelected());
      Controller.getInstance().predictMasterSwitchesBackground();
}//GEN-LAST:event_predictMasterSwitchesActionPerformed

    private void predictComplexesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_predictComplexesActionPerformed
      predictComplexes.setEnabled(false);
      predictMasterSwitches.setEnabled(false);
      syntheticPIS.setEnabled(false);
      interactionsPIS.setEnabled(false);
      managePerturbations.setEnabled(false);
      //predictFunctionalSimilaritites.setEnabled(false);
      Controller.getInstance().predictComplexesBackground();
}//GEN-LAST:event_predictComplexesActionPerformed

    private void listComplexesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listComplexesActionPerformed
      if (listComplexes.isSelected()) {
        tasksView1.activateComplexPredictionView();
      }
    }//GEN-LAST:event_listComplexesActionPerformed

    private void listMasterSwitchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listMasterSwitchesActionPerformed
      if (listMasterSwitches.isSelected()) {
        tasksView1.activateMasterSwitchesView();
      }
    }//GEN-LAST:event_listMasterSwitchesActionPerformed

    private void managePerturbationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_managePerturbationsActionPerformed
      if (managePerturbations.isSelected()) {
        tasksView1.activatePerturbationView();
      }
    }//GEN-LAST:event_managePerturbationsActionPerformed

    private void visualizeComplexesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeComplexesActionPerformed
      if (visualizeComplexes.isSelected()) {
        tasksView1.activateComplexVisualization();
      }
    }//GEN-LAST:event_visualizeComplexesActionPerformed

    private void visualizeMasterSwitchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visualizeMasterSwitchesActionPerformed
      if (visualizeMasterSwitches.isSelected()) {
        tasksView1.activateMasterSwitchVisualization();
      }
    }//GEN-LAST:event_visualizeMasterSwitchesActionPerformed

    private void exportVisualizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportVisualizationActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("GraphML Visualization (.graphml)", "graphml"));
      File f = FileChooser.saveFile();

      if (f != null) {
        try {
          tasksView1.getCurrentVisualization().saveToGraphML(f);

        } catch (IOException ex) {
          System.err.println(ex);
          JOptionPane.showMessageDialog(this.getComponent(), "Error exporting to GraphML");
        }
      }
    }//GEN-LAST:event_exportVisualizationActionPerformed

    private void exportVisualizationToPNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportVisualizationToPNGActionPerformed
      FileChooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphic (.png)", "png"));
      File f = FileChooser.saveFile();

      if (f != null) {
        try {
          tasksView1.getCurrentVisualization().saveToPNG(f);

        } catch (IOException ex) {
          System.err.println(ex);
          JOptionPane.showMessageDialog(this.getComponent(), "Error exporting to PNG");
        }
      }
    }//GEN-LAST:event_exportVisualizationToPNGActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem exportMasterSwitches;
  private javax.swing.JMenu exportMenu;
  private javax.swing.JMenuItem exportTsv;
  private javax.swing.JMenuItem exportVisualization;
  private javax.swing.JMenuItem exportVisualizationToPNG;
  private javax.swing.JCheckBox interactionsPIS;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JToggleButton listComplexes;
  private javax.swing.JToggleButton listMasterSwitches;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JToggleButton managePerturbations;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem open;
  private javax.swing.JMenuItem options;
  private javax.swing.JButton predictComplexes;
  private javax.swing.JButton predictMasterSwitches;
  private javax.swing.JProgressBar progressBar;
  private sidebar.Sidebar sidebar1;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  private javax.swing.JCheckBox syntheticPIS;
  private javax.swing.ButtonGroup tasks;
  private proteinhypernetwork.TasksView tasksView1;
  private javax.swing.JToggleButton visualizeComplexes;
  private javax.swing.JToggleButton visualizeMasterSwitches;
  // End of variables declaration//GEN-END:variables
  private final Timer messageTimer;
  private JDialog aboutBox;
  private boolean lastPISSynthetic;
}
