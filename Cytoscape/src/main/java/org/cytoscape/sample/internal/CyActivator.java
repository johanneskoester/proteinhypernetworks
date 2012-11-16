package org.cytoscape.sample.internal;

import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.sample.internal.MyPHNPanel;
import org.cytoscape.sample.internal.MyConstraintPanel;
import org.cytoscape.sample.internal.MyDomainInteractionPanel;
import org.cytoscape.sample.internal.MyResultPanel;
import org.cytoscape.sample.internal.MyProject;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;

import java.util.Properties;
import sun.misc.*;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {

		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		
		// alle eigenen Komponenten erzeugen
		MyPHNPanel myPHNPanel = new MyPHNPanel();
		MyConstraintPanel myConstraintPanel = new MyConstraintPanel();
		MyDomainInteractionPanel myDomainPanel = new MyDomainInteractionPanel();
		MyResultPanel myResultPanel = new MyResultPanel();
		// zum Starten der App
		MyProject sample02Action = new MyProject(cytoscapeDesktopService,myPHNPanel,myConstraintPanel,myDomainPanel,myResultPanel);

		registerService(bc,myPHNPanel,CytoPanelComponent.class, new Properties());
		registerService(bc,myConstraintPanel,CytoPanelComponent.class, new Properties());
		registerService(bc,myDomainPanel,CytoPanelComponent.class, new Properties());
		registerService(bc,myResultPanel,CytoPanelComponent.class, new Properties());
		registerService(bc,sample02Action,CyAction.class, new Properties());

		

	}
}

