package org.cytoscape.sample.internal;


import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.Icon;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;


public class MyConstraintPanel extends JPanel implements CytoPanelComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7222399364770735872L;
	private MyProject myProject;
	
	public MyConstraintPanel() {
		
		//eines der beiden Panel unten; hier werden Constraints angelegt
		//mit WindowBuilderEditor erzeugt
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JTextField textField = new JTextField();
		panel_2.add(textField);
		textField.setColumns(10);
		
		JButton btnEdit = new JButton("Edit");
		panel_2.add(btnEdit, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{120, 0, 0, 120, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{25, 25, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0};
		panel.setLayout(gbl_panel);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.weightx = 1.0;
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);
		
		JLabel lblOnlyIf = new JLabel("only if");
		GridBagConstraints gbc_lblOnlyIf = new GridBagConstraints();
		gbc_lblOnlyIf.insets = new Insets(0, 0, 5, 5);
		gbc_lblOnlyIf.gridx = 1;
		gbc_lblOnlyIf.gridy = 0;
		panel.add(lblOnlyIf, gbc_lblOnlyIf);
		
		JCheckBox chckbxNot = new JCheckBox("not");
		GridBagConstraints gbc_chckbxNot = new GridBagConstraints();
		gbc_chckbxNot.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNot.gridx = 2;
		gbc_chckbxNot.gridy = 0;
		panel.add(chckbxNot, gbc_chckbxNot);
		
		JComboBox comboBox_1 = new JComboBox();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.weightx = 1.0;
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 3;
		gbc_comboBox_1.gridy = 0;
		panel.add(comboBox_1, gbc_comboBox_1);
		
		JButton button = new JButton("-");
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 4;
		gbc_button.gridy = 0;
		panel.add(button, gbc_button);
		
		JButton btnOr = new JButton("or");
		GridBagConstraints gbc_btnOr = new GridBagConstraints();
		gbc_btnOr.insets = new Insets(0, 0, 5, 5);
		gbc_btnOr.gridx = 5;
		gbc_btnOr.gridy = 0;
		panel.add(btnOr, gbc_btnOr);
		
		JButton btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSave.gridx = 6;
		gbc_btnSave.gridy = 1;
		panel.add(btnSave, gbc_btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 5, 0);
		gbc_btnCancel.gridx = 7;
		gbc_btnCancel.gridy = 1;
		panel.add(btnCancel, gbc_btnCancel);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JButton button_1 = new JButton("+");
		panel_1.add(button_1);
		
		JButton button_2 = new JButton("-");
		panel_1.add(button_2);
		
		JTextField txtSearch = new JTextField();
		txtSearch.setText("search...");
		panel_1.add(txtSearch);
		txtSearch.setColumns(10);
		
		JLabel lblConstraints = new JLabel("Constraints: ");
		panel_1.add(lblConstraints);
		
		Component rigidArea = Box.createRigidArea(new Dimension(200, 20));
		panel_1.add(rigidArea);
		
		
		
		
	}
	
	public void setMyProject(MyProject myProject){
		this.myProject = myProject;
	}
	
	public Component getComponent() {
		// TODO abgeschrieben
		return this;
	}

	public CytoPanelName getCytoPanelName() {
		//soll nach unten
		return CytoPanelName.SOUTH;
	}

	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return "Constraints";
	}

}
