package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

public class Chat {
	private static int idCounter = 0;
	private Vector<ServerClient> players = new Vector<>();
	private String id;
	
	/**
	 * Creating a new game with the default type open
	 */
    public Chat() {
    	this.id = String.valueOf(idCounter++);
    }
    

    
}