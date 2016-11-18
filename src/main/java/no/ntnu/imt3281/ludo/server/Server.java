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
    private ServerMessageReader reader;
    
    /**
     * Main function to start the program
     * @param args
     */
	public static void main(String[] args) {
        try {
			new Server();
		} catch (IOException e) {System.out.println(e);}
	}
    
    
    /**
     * Constructor for the server, Creating a socket and accepting new clients
     * @throws IOException
     */
    public Server() throws IOException {
    	this.reader = new ServerMessageReader(this);
        new Thread(reader).start();

    	@SuppressWarnings("resource")
		ServerSocket listener = new ServerSocket(9090);
    	System.out.println("new socket");
        while(true) {
        	Socket socket = listener.accept();
        	clients.add(new ServerClient(socket));
        	System.out.println("new connection");
        }
    }

	/**
	 * Removing a client that have disconnected
	 * @param client the client to remove
	 */
	public void removeClient(ServerClient client) {
		clients.remove(client);
	}
	
	/**
	 * Passing along message depending on the message type
	 * @param message to be passed along
	 */
	public void sendMessage(Message message) {
		if (message.isGame())
			sendMessageToGame(message.getGameMessage());
		else if (message.isChat())
			sendMessageToChat(message);
	}
	
	/**
	 * Sending a message object to the game the message belongs to
	 * @param message the message object to send
	 */
	private void sendMessageToGame(GameMessage message) {
		if (message.isNewGameRequest()){
			boolean gameFound = false;
			for (Game game : games) {
				if (game.isJoinableByClient(message.getClient())) {
					game.addPlayer(message.getClient());
					gameFound = true;
					if (game.isFull()) {
						game.startGame();
					}
					/********************************************************/
					if (game.getPlayers() > 1) {
						game.startGame();
					}
					/********************************************************/
				}
			}
			if (!gameFound){
				Game game = new Game();
				game.addPlayer(message.getClient());
				games.add(game);
			}
		} else {
			for (Game game : games) {
				if (game.getId().equals(message.getId())) {
					game.runMessage(message);
				}
			}
		}
		
	}
	
	/**
	 * Sending a message object to the chat the message belongs to
	 * @param message the message object to send
	 */
	private void sendMessageToChat(Message message) {
		for (Chat chat : chats) {
			if (chat.getId().equals(message.getId())) {
				chat.runMessage(message);
			}
		}
	}
	
	/**
	 * @return a vector with all connected clients
	 */
	public Vector<ServerClient> getClients() {
		return clients;
	}

}
