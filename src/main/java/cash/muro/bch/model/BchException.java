package cash.muro.bch.model;

public class BchException extends Exception {

	public BchException(Exception e) {
		super(e);
	}
	
	public BchException(String message) {
		super(message);
	}
	
	public BchException(String message, Exception e) {
		super(message, e);
	}

}
