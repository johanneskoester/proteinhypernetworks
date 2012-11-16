package org.cytoscape.sample.internal;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JList;
//import javax.xml.stream.XMLStreamException;

//import proteinHypernetwork.ProteinHypernetwork;
//import proteinHypernetwork.decoder.HypernetworkMLDecoder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import java.io.File;
//import java.io.FileNotFoundException;

//import proteinHypernetwork.ProteinHypernetwork;
//import proteinHypernetwork.decoder.*;
//import logicProteinHypernetwork.*;

public class TestPanel3 extends JPanel
{

	
	public TestPanel3() {
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
				try {
//					LogicProteinHypernetwork lph = new LogicProteinHypernetwork(null, 1);
//					HypernetworkMLDecoder decoder = null;//new HypernetworkMLDecoder();
//					File file = new File("name");
					/*try {
						decoder.decode(file, null);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (XMLStreamException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					System.out.println("done");
				} catch (Exception e){
					System.out.println("Fehler");
					e.printStackTrace();
				}
				
			}
		});
		add(btnTestbutton, BorderLayout.NORTH);
		
	}

}
