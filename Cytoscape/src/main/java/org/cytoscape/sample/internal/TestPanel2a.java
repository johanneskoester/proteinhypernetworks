package org.cytoscape.sample.internal;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
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

public class TestPanel2a extends JPanel {



	/**
	 * Create the panel.
	 */
	public TestPanel2a() {
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
		add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{144, 0, 144, 120, 0, 0};
		gbl_panel.rowHeights = new int[]{25, 25, 25, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		panel.setLayout(gbl_panel);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.weightx = 1.0;
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 1, 5);
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);
		
		JLabel lblPP = new JLabel("pp");
		GridBagConstraints gbc_lblPP = new GridBagConstraints();
		gbc_lblPP.insets = new Insets(0, 0, 5, 5);
		gbc_lblPP.gridx = 1;
		gbc_lblPP.gridy = 0;
		panel.add(lblPP, gbc_lblPP);
		
		JComboBox comboBox_1 = new JComboBox();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.weightx = 1.0;
		gbc_comboBox_1.insets = new Insets(0, 0, 1, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 2;
		gbc_comboBox_1.gridy = 0;
		panel.add(comboBox_1, gbc_comboBox_1);
		
		JCheckBox chckbxdd1 = new JCheckBox("Define Domain");
		GridBagConstraints gbc_chckbxdd1 = new GridBagConstraints();
		gbc_chckbxdd1.insets = new Insets(0, 0, 1, 5);
		gbc_chckbxdd1.gridx = 0;
		gbc_chckbxdd1.gridy = 1;
		panel.add(chckbxdd1, gbc_chckbxdd1);
		
		JCheckBox checkbxdd2 = new JCheckBox("Define Domain");
		GridBagConstraints gbc_checkbxdd2 = new GridBagConstraints();
		gbc_checkbxdd2.insets = new Insets(0, 0, 1, 5);
		gbc_checkbxdd2.gridx = 2;
		gbc_checkbxdd2.gridy = 1;
		panel.add(checkbxdd2, gbc_checkbxdd2);
		
		JTextField textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 2;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JTextField textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 2;
		gbc_textField_2.gridy = 2;
		panel.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 3;
		panel.add(btnSave, gbc_btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 5, 0);
		gbc_btnCancel.gridx = 5;
		gbc_btnCancel.gridy = 3;
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
		
		JLabel lblInteractions = new JLabel("Interactions: ");
		panel_1.add(lblInteractions);
		
		Component rigidArea = Box.createRigidArea(new Dimension(200, 20));
		panel_1.add(rigidArea);
		
		

	}
}
