package no.ntnu.imt3281.ludo.logic;


public class IllegalPlayerNameException extends RuntimeException {
	public IllegalPlayerNameException(String message) {
		super(message);
	}
}
