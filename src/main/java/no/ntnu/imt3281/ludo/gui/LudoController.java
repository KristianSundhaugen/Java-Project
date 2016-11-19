package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import no.ntnu.imt3281.ludo.client.Connection;

public class LudoController {

    @FXML
    private MenuItem random;

    @FXML
    private TabPane tabbedPane;

    @FXML
    public void joinRandomGame(ActionEvent e) { 
    	LudoController controller = this;
       	Connection.newGame(controller);
    }
    public void createNewGame(String gameId, int playerNumber){
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
    	loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));
    	

		
		// Use controller to set up communication for this game.
		// Note, a new game tab would be created due to some communication from the server
		// This is here purely to illustrate how a layout is loaded and added to a tab pane.
		
    	try {
    		AnchorPane gameBoard = loader.load();
    		
    		GameBoardController controller = loader.getController();
    		
    		controller.setId(gameId);
    		controller.setPlayerNumber(playerNumber);
    		
        	Tab tab = new Tab("Game");
    		tab.setContent(gameBoard);
        	tabbedPane.getTabs().add(tab);
    	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
