package no.ntnu.imt3281.ludo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javafx.application.Platform;
import no.ntnu.imt3281.ludo.gui.GameBoardController;
import no.ntnu.imt3281.ludo.gui.LudoController;
import no.ntnu.imt3281.ludo.server.GameMessage;
import no.ntnu.imt3281.ludo.server.Message;

public class Connection {
    private static class SynchronizedHolder {
    	static Connection instance = new Connection();
    	static LudoController waitingNewGame = null;
    }
	
    private Socket socket;
	private PrintWriter output;
	private Vector<GameBoardController> games = new Vector<>();
	private Thread reader;
	
	private Connection() {
    	String serverAddress = "localhost";
        try {
			socket = new Socket(serverAddress, 9090);
			output = new PrintWriter(socket.getOutputStream(), true);
			this.reader = new Thread(new ClientMessageReader(this, socket));
			reader.start();	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The instance of the connection
	 * @return
	 */
	public static Connection getConnection() {
		return SynchronizedHolder.instance;
	}
	
	/**
	 * Adding a game to the list of games
	 * @param game the game to add
	 */
	public static void addGame(GameBoardController game) {
		getConnection().games.add(game);
	}
	
    
    /**
     * Sending messages to the server
     * @param message the message to send
     * @param type the type of message to send GAME/CHAT
     * @param id the id of the game/chat
     */
	public static void sendMessage(String message, String type, String id) {
		getConnection().sendServerMessage(new Message(message, type, id));
	}
	
	/**
	 * Sending a message to the output stream
	 * @param msg the message to sent to the server
	 */
	private void sendServerMessage(Message msg) {
		System.out.println("Clent Sending: ->" + msg.toString());
		output.println(msg.toString());
	}
	

	/**
	 * Parsing a incoming message and sending the message
	 * sending message to the game parser if its a game message and
	 * adding message to chat if its a chat message
	 * @param msg the message received from the server
	 */
	public void messageParser(Message msg) {
		if (msg.isChat()){
			// TODO add message to chat window
		} else if (msg.isGame()) {
			sendGameMessage(msg.getGameMessage());			
		}
	}
	
	/**
	 * Creating a new game if it is a new game message
	 * sending the message to the game if the game already exists
	 * @param gmsg the game message 
	 */
	private void sendGameMessage(GameMessage gmsg) {
		if (gmsg.isNewGame()) {
			LudoController controller = SynchronizedHolder.waitingNewGame;
			Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	controller.createNewGame(gmsg.getId());
	            }
			});
			SynchronizedHolder.waitingNewGame = null;
		} else {
			for (GameBoardController game : games) {
				if (game.getId() == gmsg.getId())
					game.gameMessage(gmsg);
			}
		}
	}
	
	/**
	 * Requesting server to create a new game from this controller
	 * @param ludoControllern the controller
	 */
	public static void newGame(LudoController ludoController) {
		SynchronizedHolder.waitingNewGame = ludoController;
		sendMessage("NEW_RANDOM_GAME_REQUEST", "GAME", "-1");
	}
	
	/**
	 * Stopping the reader from reading from the socket
	 */
	public static void stopConnection() {
		getConnection().reader.stop();
	}
}
