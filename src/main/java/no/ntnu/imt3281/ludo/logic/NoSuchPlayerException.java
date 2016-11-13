package no.ntnu.imt3281.ludo.logic;


public class NoSuchPlayerException extends RuntimeException {
	public NoSuchPlayerException(String message) {
		super(message);
	}
}
