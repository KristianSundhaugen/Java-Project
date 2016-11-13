package no.ntnu.imt3281.ludo.logic;


public class NoRoomForMorePlayersException extends RuntimeException {	
	public NoRoomForMorePlayersException(String message){
		super(message);
	}
}