package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import no.ntnu.imt3281.I18N.I18N;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.client.Globals;
import no.ntnu.imt3281.ludo.server.GameMessage;

/**
 * Controller for the main window of the applications, handling the menu actions
 * @author Lasse Sviland
 *
 */
public class LudoController {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	@FXML
	private MenuItem random;

	@FXML
	private TabPane tabbedPane;
	
	@FXML 
	private Label loggedInMessage;
	
	@FXML
	private MenuItem connectButton;
	
	/**
	 * Constructor for the controller, setting this as the ludo controller for the Connection
	 */
	public LudoController() {
		Connection.setLudoController(this);
	}

	/**
	 * Response to user clicking join chat room button
	 */
	@FXML
	public void joinChatRoom() {
		if (!Connection.isLoggedIn()) {
			displayLogin(); 
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewChat.fxml"));
			loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
			AnchorPane newChat = loader.load();
			NewChatController controller = loader.getController();
			Tab tab = new Tab(I18N.getBundle().getString("newchat.create"));
			controller.setLudoController(this, tab);
			tab.setContent(newChat);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "joinChatRoom", e);
		}
	}
	
	/**
	 * Response to user clicking list chat room button
	 */
	@FXML
	public void listChatRooms() {
		if (!Connection.isLoggedIn()) {
			displayLogin(); 
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ListRooms.fxml"));
			loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
			AnchorPane listRooms = loader.load();
			ListRoomsController controller = loader.getController();
			Tab tab = new Tab(I18N.getBundle().getString("chatlist"));
			controller.setLudoController(this, tab);
			tab.setContent(listRooms);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "listChatRooms", e);
		}
	}
	
	/**
	 * Response to user clicking challenge player button
	 */
	@FXML
	public void challengePlayers() {
		if (!Connection.isLoggedIn()) {
			displayLogin(); 
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InvitePlayers.fxml"));
			loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
			AnchorPane listRooms = loader.load();
			InvitePlayerController controller = loader.getController();
			Tab tab = new Tab(I18N.getBundle().getString("iviteplayers"));
			controller.setLudoController(this, tab);
			tab.setContent(listRooms);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "challengePlayers", e);
		}
	}
	
	/**
	 * When user clicks on connect, the login screen will be displayed
	 */
	@FXML
	public void displayLogin() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
			Pane login = loader.load();
			LoginController controller = loader.getController();
			Tab tab = new Tab(I18N.getBundle().getString("login"));
			controller.setLudoController(this, tab);
			tab.setContent(login);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch(IOException e) {
			logger.throwing(this.getClass().getName(), "displayLogin", e);
		}
	}
	
	
	/**
	 * Telling the server that the user wants to join a new random game
	 * @param e the event triggered when the user clicked join random game
	 */
	@FXML
	public void joinRandomGame() { 
		if (!Connection.isLoggedIn()) {
			displayLogin(); 
			return;
		}
		Connection.sendMessage("NEW_RANDOM_GAME_REQUEST", "GAME", "-1");
	}
	
	/**
	 * Removing a tab from the TabPane
	 * @param tab the tab to remove
	 */
	public void removeTab(Tab tab) {
		tabbedPane.getTabs().remove(tab);
	}
	
	
	/**
	 * Creating a new tab for the game, and sending it the correct information
	 * @param gameId the id for the new game
	 * @param playerNumber the player number the client is representing i the new game
	 */
	public void createNewGame(String gameId, int playerNumber){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
		loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

		try {
			AnchorPane gameBoard = loader.load();
			
			GameBoardController controller = loader.getController();
			
			controller.setId(gameId);
			controller.setPlayerNumber(playerNumber);
			controller.setPane(gameBoard);
			Tab tab = new Tab(I18N.getBundle().getString("ludo.game") + " " + gameId);
			tab.setContent(gameBoard);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "createNewGame", e);
		}
	}
	
	/**
	 * Creating a new tab with only chat content
	 * @param id the id of the game containing the chat
	 * @param name the name for the chat
	 */
	public void createNewChatTab(String id, String name) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
		loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
		try {
			AnchorPane gameBoard = loader.load();
			GameBoardController controller = loader.getController();
			
			controller.setChat();
			controller.setId(id);
			controller.setPane(gameBoard);
			Tab tab = new Tab(name);
			tab.setContent(gameBoard);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "createNewChatTab", e);
		}
	}

	/**
	 * Showing invite dialog
	 * @param gameMessage starting the tab where the user can invite other players to a game
	 */
	public void showInviteDialog(GameMessage gameMessage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Invite.fxml"));
			loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
			AnchorPane invitePane = loader.load();
			InviteController controller = loader.getController();
			Tab tab = new Tab(I18N.getBundle().getString("invite"));
			controller.setLudoController(this, tab);
			controller.setInviteInfo(gameMessage);
			tab.setContent(invitePane);
			tabbedPane.getTabs().add(tab);
			tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "showInviteDialog", e);
		}
	}

	/**
	 * Setting the logged in message with the logged in username
	 * @param username the username to have in the message
	 */
	public void setLoginMessage(String username) {
		this.loggedInMessage.setText(I18N.getBundle().getString("ludo.loggedin") + " " + username);
	}

	/**
	 * When the user hits the close button in the menu and the application should close
	 * when the javafx thread is closed it will trigger close functions for the connection
	 */
	@FXML
	public void closeButton() {
		Connection.stopConnection();
		Stage stage = (Stage) loggedInMessage.getScene().getWindow();
		stage.close();

	}

	/**
	 * Disabeling the connect button, used for not letting a user connect multiple times
	 */
	public void disableConnect() {
		this.connectButton.setDisable(true);
	}
}
