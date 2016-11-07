package no.ntnu.imt3281.ludo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * 
 * This is the main class for the server. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Server  {
	private Vector<ServerClient> clients  = new Vector<>();
	private Vector<Game> games = new Vector<>();
	private Vector<Chat> chats = new Vector<>();
    private MessageReader reader;
    public Server() throws IOException {
    	this.reader = new MessageReader(this);
        new Thread(reader).start();

    	ServerSocket listener = new ServerSocket(9090);
    	System.out.println("new socket");
        while(true) {
        	Socket socket = listener.accept();
        	clients.add(new ServerClient(socket));
        	System.out.println("new connection");
        }

    }
	public static void main(String[] args) {
        try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Vector<ServerClient> getClients() {
		return clients;
	}
	public void removeClient(ServerClient client) {
		clients.remove(client);
	}

}
