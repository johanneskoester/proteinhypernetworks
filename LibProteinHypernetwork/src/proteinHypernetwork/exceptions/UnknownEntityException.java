package proteinHypernetwork.exceptions;

public class UnknownEntityException extends Exception {

	public UnknownEntityException(String id) {
		super("Network entity " + id + " does not exist in the Hypernetwork.");
	}
}
