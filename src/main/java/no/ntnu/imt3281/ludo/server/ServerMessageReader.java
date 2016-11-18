package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

public class ServerMessageReader implements Runnable {
    private Server server;
	
    public ServerMessageReader(Server server) {
		this.server = server;
	}
	
	public void run() {
		while (true) {
			Vector<ServerClient> clients = server.getClients();
		    System.out.println(clients.size());
			for (int i = 0; i < clients.size(); i++){
				Message msg = clients.get(i).getMessage();
				if (msg != null)
					parseMessage(msg);
			}
			
			try {Thread.sleep(100);} catch (InterruptedException e) {}
		}    	
	}
	
	
	private void parseMessage(Message message) {
		if (message.isDisconnected()) {
			server.removeClient(message.getClient());
		} else if (message.isGame() || message.isChat()){
			server.sendMessage(message);		
		}
			
	}
			
}
