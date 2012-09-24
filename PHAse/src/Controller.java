import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import logicProteinHypernetwork.LogicProteinHypernetwork;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;


public class Controller {
	private static Controller instance = new Controller();
	
	private int threads = 1;
	private ProteinHypernetwork hypernetwork;
	private LogicProteinHypernetwork logicHypernetwork;
	
	public static Controller getInstance() {
		return instance;
	}
	
	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public void loadHypernetwork(File file) throws FileNotFoundException, XMLStreamException {
		HypernetworkMLDecoder decoder = new HypernetworkMLDecoder();
		hypernetwork = new ProteinHypernetwork();
		decoder.decode(file, hypernetwork);
		logicHypernetwork = new LogicProteinHypernetwork(hypernetwork, threads);
	}
}
