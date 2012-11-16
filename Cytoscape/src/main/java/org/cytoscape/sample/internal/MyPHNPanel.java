package org.cytoscape.sample.internal;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import java.awt.Font;

public class MyPHNPanel extends JPanel implements CytoPanelComponent {
	
	private static final long serialVersionUID = 8292806967891823933L;
	private MyProject myProject;

	public MyPHNPanel() {
		
		this.setBackground(Color.white);
		
		this.setVisible(true);

		//Panel links; 
		//mit WindowBuilderEditor erzeugt
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		
		JLabel lblConstraints = new JLabel("Constraints");
		lblConstraints.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnEdit = new JButton("Edit");
		panel_1.add(btnEdit);
		
		JButton btnVisualize = new JButton("Visualize");
		panel_1.add(btnVisualize);
		
		JLabel lblProteinComplexes = new JLabel("Protein Complexes");
		lblProteinComplexes.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JPanel panel_3 = new JPanel();
		
		JLabel lblPertubationImpactScore = new JLabel("Pertubation Impact Score");
		lblPertubationImpactScore.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JPanel panel_2 = new JPanel();
		
		JLabel lblPertubation = new JLabel("Pertubation");
		lblPertubation.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JPanel panel_5 = new JPanel();
		
		JPanel panel_4 = new JPanel();
		
		JButton btnPredict_2 = new JButton("Predict");
		panel_4.add(btnPredict_2);
		
		JButton btnList_2 = new JButton("List");
		panel_4.add(btnList_2);
		
		JButton btnVisualize_2 = new JButton("Visualize");
		panel_4.add(btnVisualize_2);
		
		JLabel lblInteractions = new JLabel("Interactions");
		lblInteractions.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton button = new JButton("Edit");
		panel_6.add(button);
		
		JButton button_1 = new JButton("Visualize");
		panel_6.add(button_1);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblProteinComplexes))
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblConstraints, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPertubationImpactScore))
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPertubation))
						.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblInteractions)
							.addPreferredGap(ComponentPlacement.RELATED, 164, Short.MAX_VALUE))
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(12)	
					.addComponent(lblInteractions)
					.addGap(6)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(32)
					.addComponent(lblConstraints, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(44)
					.addComponent(lblProteinComplexes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(38)
					.addComponent(lblPertubationImpactScore)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(38)
					.addComponent(lblPertubation)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		
		JButton btnManage = new JButton("Manage");
		panel_5.add(btnManage);
		
		JCheckBox chckbxSynthetic = new JCheckBox("synthetic");
		panel_2.add(chckbxSynthetic);
		
		JCheckBox chckbxInteractions = new JCheckBox("interactions");
		panel_2.add(chckbxInteractions);
		
		JButton btnPredict_1 = new JButton("Predict");
		panel_3.add(btnPredict_1);
		
		JButton btnList_1 = new JButton("List");
		panel_3.add(btnList_1);
		
		JButton btnVisualize_1 = new JButton("Visualize");
		panel_3.add(btnVisualize_1);
		panel.setLayout(gl_panel);

		
		
/*		
		Box box = Box.createVerticalBox();
		Box innerBox1 = Box.createHorizontalBox();
		
		JLabel lbConstraints = new JLabel("Constraints");
		box.add(lbConstraints);
		Component glue = box.createGlue();
		box.add(glue);
		
		box.add(innerBox1);
		
		JButton bAddCons = new JButton("Add");
		innerBox1.add(bAddCons);
		JButton bVisCons = new JButton("Visualize");
		innerBox1.add(bVisCons);
		
		box.setVisible(true);
		
		this.add(box);
		
*/	
		}

	public void setMyProject(MyProject myProject){
		this.myProject = myProject;
	}
	
	public Component getComponent() {
		return this;
	}


	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}


	public String getTitle() {
		return "ProteinHyperNetworks";
	}


	public Icon getIcon() {
		return null;
	}
}
