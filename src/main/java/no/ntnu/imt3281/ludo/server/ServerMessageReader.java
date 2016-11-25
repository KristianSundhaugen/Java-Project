package no.ntnu.imt3281.ludo.server;


import java.util.ArrayList;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Used to read messages sent to the server from the clients
 * @author Lasse
 */
public class ServerMessageReader implements Runnable {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);
    private Server server;
    private boolean stop = false;
	
    public ServerMessageReader(Server server) {
		this.server = server;
	}
	
	public void run() {
		while (!stop) {
			ArrayList<ServerClient> clients = server.getClients();
			for (int i = 0; i < clients.size(); i++){
				Message msg = clients.get(i).getMessage();
				if (msg != null)
					parseMessage(msg);
			}
			
			try {Thread.sleep(100);} catch (InterruptedException e) {
				logger.throwing(this.getClass().getName(), "run", e);
				Thread.currentThread().interrupt();
			}
		}    	
	}
	
	
	private void parseMessage(Message message) {
		
		if (message.isDisconnected()) {
			server.removeClient(message.getClient());
		} else if (message.isGame() || message.isChat() || message.isUser()){
			server.parseMessage(message);		
		}
			
	}
	public void stop() {
		this.stop = true;
	}
			
}
