package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import no.ntnu.imt3281.ludo.client.Connection;
import no.ntnu.imt3281.ludo.server.ChatMessage;
import no.ntnu.imt3281.ludo.server.GameMessage;

/**
 * Controller for the main window of the applications, handling the menu actions
 * @author Lasse Sviland
 *
 */
public class LudoController {

    @FXML
    private MenuItem random;

    @FXML
    private TabPane tabbedPane;
    
    /**
     * Response to user clicking join chat room button
     */
    @FXML
    public void joinChatRoom() {
    	
    }
    
    /**
     * Response to user clicking list chat room button
     */
    @FXML
    public void listChatRooms() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("ListRooms.fxml"));
    		loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
    		AnchorPane listRooms = loader.load();
    		ListRoomsController controller = loader.getController();
        	Tab tab = new Tab("Chat List");
    		controller.setLudoController(this, tab);
    		tab.setContent(listRooms);
        	tabbedPane.getTabs().add(tab);
        	tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
    	} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    /**
     * Removing a tab from the TabPane
     * @param tab the tab to remove
     */
    public void removeTab(Tab tab) {
    	tabbedPane.getTabs().remove(tab);
    }
    
    /**
     * Telling the server that the user wants to join a new random game
     * @param e the event triggered when the user clicked join random game
     */
    @FXML
    public void joinRandomGame(ActionEvent e) { 
    	LudoController controller = this;
       	Connection.newGame(controller);
    }
    
    /**
     * The client have received information about a new game to join
     * @param gmsg the message received
     */
    public void createNewGameMessage(GameMessage gmsg) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	createNewGame(gmsg.getId(), Integer.parseInt(gmsg.getMessageValue()));
            }
		});
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
        	Tab tab = new Tab("Game " + gameId);
    		tab.setContent(gameBoard);
        	tabbedPane.getTabs().add(tab);
        	tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
    	} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

	public void createNewChatTab(String id) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
    	loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
    	try {
    		AnchorPane gameBoard = loader.load();
    		GameBoardController controller = loader.getController();
    		
    		controller.setChat();
    		controller.setId(id);
    		controller.setPane(gameBoard);
        	Tab tab = new Tab("Chat " + id);
    		tab.setContent(gameBoard);
        	tabbedPane.getTabs().add(tab);
        	tabbedPane.getSelectionModel().select(tabbedPane.getTabs().indexOf(tab));
    	} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
