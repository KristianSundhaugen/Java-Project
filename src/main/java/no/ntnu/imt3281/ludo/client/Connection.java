package no.ntnu.imt3281.ludo.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.gui.GameBoardController;
import no.ntnu.imt3281.ludo.gui.InvitePlayerController;
import no.ntnu.imt3281.ludo.gui.ListRoomsController;
import no.ntnu.imt3281.ludo.gui.LudoController;
import no.ntnu.imt3281.ludo.gui.LoginController;
import no.ntnu.imt3281.ludo.server.Message;

/**
 * Singleton class for holding a connection to the server
 * Receiving events from the game and is sending them to the server
 * @author Lasse Sviland
 */
public class Connection {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);
	private static class SynchronizedHolder {
		static Connection instance = new Connection();
		static LudoController ludoController = null;
		static ListRoomsController listRoomsController = null;
		static InvitePlayerController invitePlayerController = null;
		static LoginController loginController = null;
		static boolean loggedIn = false;
		static String username = "";
	}
	
	private Socket socket;
	private PrintWriter output;
	private ArrayList<GameBoardController> games = new ArrayList<>();
	private ClientMessageReader reader;
	
	private Connection() {
		try {
			socket = new Socket(Globals.SERVER_ADDRESS, Globals.SERVER_PORT);
			output = new PrintWriter(socket.getOutputStream(), true);
			this.reader = new ClientMessageReader(socket);
			Thread readerThread = new Thread(this.reader);
			readerThread.start();	        
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "Connection", e);
		}
	}
	
	/**
	 * The instance of the connection
	 * @return the instance of the connection
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
	public static void messageParser(Message msg) {
		
		if (msg.isGame() && msg.getGameMessage().isNewGame()) {
					SynchronizedHolder.ludoController.createNewGame(msg.getId(), msg.getGameMessage().intPart(1));
		} else if (msg.isGame() && msg.getGameMessage().isPlayerListResponse()) {
			if (SynchronizedHolder.invitePlayerController != null)
				SynchronizedHolder.invitePlayerController.playerListResponse(msg.getGameMessage());
			SynchronizedHolder.invitePlayerController = null;
		/*} else if (msg.isGame() && msg.getGameMessage().isPrivateGameResponse()) {
			if (SynchronizedHolder.invitePlayerController != null)
				SynchronizedHolder.invitePlayerController.playerListResponse(msg.getGameMessage());
			SynchronizedHolder.invitePlayerController = null;*/
		} else if (msg.isGame() && msg.getGameMessage().isGameInvite()) {
			SynchronizedHolder.ludoController.showInviteDialog(msg.getGameMessage());
		} else if (msg.isChat() && msg.getChatMessage().isListResponse()) {
			if (SynchronizedHolder.listRoomsController != null)
					SynchronizedHolder.listRoomsController.listResponse(msg.getChatMessage());
			SynchronizedHolder.listRoomsController = null;
		} else if (msg.isChat() && msg.getChatMessage().isChatJoin()) {
			SynchronizedHolder.ludoController.createNewChatTab(msg.getId(), msg.stringPart(1));
		} else if(msg.isUser() && msg.getUserMessage().isLoginRespons()){
			if (SynchronizedHolder.loginController != null)
				SynchronizedHolder.loginController.loginResponse(msg.getUserMessage());
		} else if(msg.isUser() && msg.getUserMessage().isRegisterResponse()){
			if(SynchronizedHolder.loginController != null)
				SynchronizedHolder.loginController.registerResponse(msg.getUserMessage());
		} else
			getConnection().parseGameMessage(msg);	
	}
	
	/**
	 * Sending a message to the game, looping until the game can receive the message
	 * @param msg the message to send to the game
	 */
	private void parseGameMessage(Message msg) {
		boolean run = true;
		while(run){
			for (GameBoardController game : games) {
				if (game.getId().equals(msg.getId())) {
					game.messageParser(msg);
					run = false;					
				}
			}
		}
	}
	/**
	 * Setting the ludo controller for the user
	 * @param controller
	 */
	public static void setLudoController(LudoController controller) {
		SynchronizedHolder.ludoController = controller;
	}

	/**
	 * Setting the ludo controller for the user
	 * @param controller
	 */
	public static void setLoginController(LoginController controller) {
		SynchronizedHolder.loginController = controller;

	}

	
	/**
	 * Sending request to the server to join a new chat
	 * @param ludoController the controller to respond to when the server sends and answer
	 */
	public static void newChatListRequest(ListRoomsController listController) {
		SynchronizedHolder.listRoomsController = listController;
		Connection.sendMessage("LIST", "CHAT", "-1");
	}
	
	/**
	 * Stopping the reader from reading from the socket
	 */
	public static void stopConnection() {
		getConnection().reader.stop();
	}
	
	/**
	 * request to get a player list from the server
	 * @param invitePlayerController the controller that should receive the player list
	 */
	public static void newPlayerListRequest(InvitePlayerController invitePlayerController) {
		SynchronizedHolder.invitePlayerController = invitePlayerController;
		Connection.sendMessage("PLAYER_LIST", "GAME", "-1");	
	}

	/**
	 * @param invitePlayerController the controller to respond to when there is a response from the server
	 */
	public static void newPrivateGameRequest(InvitePlayerController invitePlayerController) {
		SynchronizedHolder.invitePlayerController = invitePlayerController;
		Connection.sendMessage("PRIVATE_GAME_REQUEST", "GAME", "-1");	
	}
	
	/**
	 * Setting the user as logged in
	 * @param username
	 */
	public static void loggedIn(String username) {
		SynchronizedHolder.username = username;
		SynchronizedHolder.loggedIn = true;	
		SynchronizedHolder.ludoController.setLoginMessage(SynchronizedHolder.username);
		SynchronizedHolder.ludoController.disableConnect();
	}

	/**
	 * @return boolean telling if the user is logged in
	 */
	public static boolean isLoggedIn() {
		return SynchronizedHolder.loggedIn;
	}

}
