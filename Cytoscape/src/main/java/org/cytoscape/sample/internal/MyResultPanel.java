package org.cytoscape.sample.internal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileNotFoundException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import edu.udo.elly2.*;
//import logicProteinHypernetwork.*;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

//import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;

public class MyResultPanel extends JPanel implements CytoPanelComponent{

	public MyResultPanel() {
		
		//Panel rechts; hier werden Ergebnisse angezeigt
		//mit WindowBuilderEditor erzeugt
		
		setLayout(new BorderLayout(0, 0));
		
		JTextField txtSearch = new JTextField();
		txtSearch.setText("Search...");
		add(txtSearch, BorderLayout.SOUTH);
		txtSearch.setColumns(20);
		
		JList list = new JList();
		add(list, BorderLayout.CENTER);
		
		JButton btnTestbutton = new JButton("Testbutton");
		btnTestbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("piep");
				HypernetworkMLDecoder decoder = new HypernetworkMLDecoder();
				//File file = new File("/home/elly/ProteinHypernetworks/test.hml");
//				ProteinHypernetwork network = new ProteinHypernetwork();
//				try {
//					decoder.decode(null, network);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		
		add(btnTestbutton, BorderLayout.NORTH);
		
		
		
	}
	
	private static final long serialVersionUID = 3827547215812128399L;

	public Component getComponent() {
		// TODO abgeschrieben
		return this;
	}

	public CytoPanelName getCytoPanelName() {
		//soll nach rechts
		return CytoPanelName.EAST;
	}

	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return "Predictions";
	}
	
}
