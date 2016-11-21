package no.ntnu.imt3281.ludo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javafx.application.Platform;
import no.ntnu.imt3281.ludo.gui.GameBoardController;
import no.ntnu.imt3281.ludo.gui.LudoController;
import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.DiceListener;
import no.ntnu.imt3281.ludo.logic.PieceEvent;
import no.ntnu.imt3281.ludo.logic.PieceListener;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;
import no.ntnu.imt3281.ludo.logic.PlayerListener;
import no.ntnu.imt3281.ludo.server.GameMessage;
import no.ntnu.imt3281.ludo.server.Message;

/**
 * Singleton class for holding a connection to the server
 * Receiving events from the game and is sending them to the server
 * @author lassesviland
 */
public class Connection implements DiceListener, PieceListener, PlayerListener {
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
		System.out.println("Message Parser");
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
		System.out.println("Game Message");
		if (gmsg.isNewGame()) {
			System.out.println("New Game From Server");
			LudoController controller = SynchronizedHolder.waitingNewGame;
			Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	controller.createNewGame(gmsg.getId(), Integer.parseInt(gmsg.getMessageValue()));
	            }
			});
			SynchronizedHolder.waitingNewGame = null;
		} else {
			boolean run = true;
			while(run){
				for (GameBoardController game : games) {
					if (game.getId().equals(gmsg.getId())) {
						game.gameMessage(gmsg);
						run = false;					
					}
				}
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
	
	/**
	 * Called when a piece have been moved, 
	 * sending message to the server with the information from the event
	 * @param event the event that was called
	 */
	@Override
	public void pieceMoved(PieceEvent event) {
		sendMessage("PIECE_EVENT:" + event.getFrom() 
		+ ":"  + event.getTo() 
		+ ":" + event.getPiece() 
		+ ":" + event.getPlayer() , 
		"GAME", 
		event.getLudo().getId());		
	}

	/**
	 * Called when a dice is thrown,
 	 * sending message to the server with the information from the event
	 * @param event the event that was called from the server
	 */
	@Override
	public void diceThrown(DiceEvent event) {
		/*sendMessage("DICE_EVENT:" + event.getDice() 
		+ ":" + event.getPlayer() , 
		"GAME", 
		event.getLudo().getId());
		*/
	}
	
	/**
	 * Called when the state of a player is changed, 
	 * sending message to the server with the new state
	 */
	@Override
	public void playerStateChanged(PlayerEvent event) {
		sendMessage("PLAYER_EVENT:" + event.getState() 
		+ ":" + event.getPlayer() , 
		"GAME", 
		event.getLudo().getId());
	}
}
