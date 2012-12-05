/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import proteinHypernetwork.exceptions.UnknownEntityException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class CLI {
	public static String usage = "PHAse <input> <output>";

	@Parameter(description = "<output>")
	private List<String> parameters = new ArrayList<String>();

	@Parameter(names = { "-h", "--help" }, description = "Print this message.")
	private boolean help;

	@Parameter(names = { "-c", "--complexes" }, description = "Predict protein complexes.")
	private boolean predictComplexes;

	@Parameter(names = { "-p", "--pis" }, description = "Predict perturbation impact score.")
	private boolean predictPIS;

	@Parameter(names = { "-s", "--similarity" }, description = "Predict functional similarities.")
	private boolean predictSimilarity;
	
	@Parameter(names = { "-pe", "--perturbationeffects" }, description = "Predict perturbation effects.")
	private boolean predictPerturbationEffects;

	@Parameter(names = { "-tt", "--truthtables" }, description = "Predict interaction truth tables.")
	private boolean predictTruthTables;
	
	@Parameter(names = { "-mc", "--min-complexes" }, description = "Minimum number of complexes that have to contain a pair of proteins for truth table prediction.")
	private int minTTComplexes = 3;

	@Parameter(names = { "-t", "--threads" }, description = "Number of threads to use (1 per default).")
	private int threads = 1;

	@Parameter(names = { "-lh", "--load-hypernetwork" }, description = "Load a protein hypernetwork as a HypernetworkML (.hml) file.")
	private String hypernetwork;

	@Parameter(names = { "-ln", "--load-network" }, description = "Load a protein network as a tab separated file (1st column: protein name, 2nd column: interacting protein name).")
	private String network;

	@Parameter(names = { "-lc", "--load-complexes" }, description = "Load protein complexes (1st column: complex id, 2nd column: protein name).")
	private String complexes;
	
	@Parameter(names = { "-ptb", "--perturbations" }, description = "Proteins to perturb.")
	private List<String> perturbations = new ArrayList<String>();

	public static void main(String[] args) {
		CLI cli = new CLI();
		JCommander jc = new JCommander(cli, args);

		if (cli.help) {
			jc.usage();
			System.exit(0);
		}
		if (!(cli.predictComplexes || cli.predictTruthTables || cli.predictPIS || cli.predictSimilarity || cli.predictPerturbationEffects)) {
			jc.usage();
			System.exit(1);
		}
		if(cli.predictPerturbationEffects && cli.perturbations.isEmpty()) {
			jc.usage();
			System.exit(1);
		}
		if ((cli.predictComplexes || cli.predictPIS || cli.predictSimilarity || cli.predictPerturbationEffects)
				&& cli.hypernetwork == null) {
			jc.usage();
			System.exit(1);
		}
		if (cli.predictTruthTables
				&& (cli.network == null || cli.complexes == null)) {
			jc.usage();
			System.exit(1);
		}

		String output = cli.parameters.get(0);

		BufferedWriter outstream = null;
		if (output.equals("-")) {
			outstream = new BufferedWriter(new OutputStreamWriter(System.out));
		} else {
			try {
				outstream = new BufferedWriter(new FileWriter(output));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

		Controller.getInstance().setThreads(cli.threads);

		if (cli.predictComplexes || cli.predictPIS || cli.predictSimilarity || cli.predictPerturbationEffects) {
			File inputFile = new File(cli.hypernetwork);
			try {
				Controller.getInstance().loadHypernetwork(inputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}

			try {
				if (cli.predictComplexes) {
					Controller.getInstance().predictComplexes(outstream);
				} else if (cli.predictPIS) {
					Controller.getInstance().predictPIS(outstream);
				} else if (cli.predictSimilarity) {
					Controller.getInstance().predictFunctionalSimilarities(
							outstream);
				} else if (cli.predictPerturbationEffects) {
					Controller.getInstance().predictPerturbationEffects(cli.perturbations, outstream);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				System.exit(1);
			} catch(UnknownEntityException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
		else if (cli.predictTruthTables) {
			if(output.equals("-")) {
				jc.usage();
				System.exit(1);
			}
			Controller.getInstance().predictTruthTables(cli.network, cli.complexes, new File(output), cli.minTTComplexes);
		}
		System.exit(0);
	}
}
