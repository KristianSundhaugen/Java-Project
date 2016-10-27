package no.ntnu.imt3281.ludo.logic;

public class NoRoomForMorePlayersException extends Exception {	
	public NoRoomForMorePlayersException(String message){
		super(message);
	}
}