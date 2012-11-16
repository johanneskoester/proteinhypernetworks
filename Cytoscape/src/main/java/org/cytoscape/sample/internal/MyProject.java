package org.cytoscape.sample.internal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.JButton;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;

//import proteinHypernetwork.constraints.*;
//import logicProteinHypernetwork.*;

public class MyProject extends AbstractCyAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CySwingApplication desktopApp;
	private final CytoPanel cytoPanelWest;
	private final CytoPanel cytoPanelSouth;
	private final CytoPanel cytoPanelEast;
	private MyPHNPanel myPHNPanel;
	private MyConstraintPanel myConstraintPanel;
	private MyDomainInteractionPanel myDomainPanel;
	private MyResultPanel myResultPanel;
	public int foo;


	public MyProject(CySwingApplication desktopApp,
			MyPHNPanel myPHNPanel, MyConstraintPanel myConstraintPanel, MyDomainInteractionPanel myDomainPanel, MyResultPanel myResultPanel){
		// Add a menu item -- Apps->sample02
		super("MyProject");
		setPreferredMenu("Apps");

		this.desktopApp = desktopApp;
		
		//Note: myCytoPanel is bean we defined and registered as a service
		this.cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
		this.myPHNPanel = myPHNPanel;
		this.cytoPanelSouth = this.desktopApp.getCytoPanel(CytoPanelName.SOUTH);
		this.myConstraintPanel= myConstraintPanel;	
		this.myDomainPanel = myDomainPanel;
		this.cytoPanelEast = this.desktopApp.getCytoPanel(CytoPanelName.EAST);
		this.myResultPanel= myResultPanel;	
		
		myPHNPanel.setMinimumSize(new Dimension(400,400));
		myResultPanel.setMinimumSize(new Dimension(400,400));
		myPHNPanel.setMyProject(this);
		
		System.out.println("start");
		
	}
	
	// wird beim Starten der App ausgeführt
	public void actionPerformed(ActionEvent e) {
		// If the state of the cytoPanelWest is HIDE, show it
		if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
			cytoPanelWest.setState(CytoPanelState.DOCK);
		} 
		if (cytoPanelSouth.getState() == CytoPanelState.HIDE) {
			cytoPanelSouth.setState(CytoPanelState.DOCK);
		} 
		if (cytoPanelEast.getState() == CytoPanelState.HIDE) {
			cytoPanelEast.setState(CytoPanelState.DOCK);
		} 
		
		// Select my cytoPanel
		int index = cytoPanelWest.indexOfComponent(myPHNPanel);
		System.out.print("Westindex");
		System.out.println(index);	
		if (index != -1) {
			cytoPanelWest.setSelectedIndex(index);	
		}
			
		// Select my constraintPanel
		index = cytoPanelSouth.indexOfComponent(myConstraintPanel);
		System.out.print("Southindex");
		System.out.println(index);		
		if (index == -1) {
			return;
		}
		cytoPanelSouth.setSelectedIndex(index);		
		
		// Select my domainPanel
		index = cytoPanelSouth.indexOfComponent(myDomainPanel);
		System.out.print("Southindex");
		System.out.println(index);		
		if (index == -1) {
			return;
		}
		cytoPanelSouth.setSelectedIndex(index);
		
		// Select my resultPanel
		index = cytoPanelEast.indexOfComponent(myResultPanel);
		System.out.print("Eastindex");
		System.out.println(index);		
		if (index == -1) {
			return;
		}
		cytoPanelEast.setSelectedIndex(index);
	}
	

	public MyPHNPanel getMyPHNPanel() {
		return myPHNPanel;
	}

	public MyConstraintPanel getMyConstraintPanel() {
		return myConstraintPanel;
	}	
	
	public MyResultPanel getMyResultPanel() {
		return myResultPanel;
	}
	
	public MyDomainInteractionPanel getMyDomainInteractionPanel() {
		return myDomainPanel;
	}

}
